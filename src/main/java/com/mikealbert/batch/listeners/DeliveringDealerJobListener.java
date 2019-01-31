package com.mikealbert.batch.listeners;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;

public class DeliveringDealerJobListener implements StepExecutionListener  {

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Resource StepExecutionListenerCtxInjector ctxInjector;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private String make;
	private Long parentProviderId;
	private String folderName;
	
	@Value("#{jobParameters[make]}")
	public void setMake(String make) {
		this.make = make;
	}
	public String getMake() {
		return make;
	}	
	
	@Value("#{jobParameters[parentProviderId]}")
	public void setParentProviderId(Long parentProviderId) {
		this.parentProviderId = parentProviderId;
	}
	
	@Value("#{jobParameters[folderName]}")
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFolderName() {
		return folderName;
	}
	
	@Override
	@Transactional 
	public void beforeStep(StepExecution stepExecution) {
		String resetFlagQuery =  null;
		try{
			if(make.equalsIgnoreCase("toyota")){
				resetFlagQuery = "UPDATE WILLOW2K.potential_supplier SET NCV_BATCH = 'N', CV_BATCH = 'N' WHERE mak_mak_id = " + parentProviderId ;
			}else if(make.equalsIgnoreCase("nissan")){
				if(folderName.toLowerCase().contains("ncv")){
					resetFlagQuery = "UPDATE WILLOW2K.potential_supplier SET NCV_BATCH = 'N' WHERE mak_mak_id = " + parentProviderId ;
				}else{
					resetFlagQuery = "UPDATE WILLOW2K.potential_supplier SET CV_BATCH = 'N' WHERE mak_mak_id = " + parentProviderId ;
				}
			}
			if(resetFlagQuery != null){
				Query q = entityManager.createNativeQuery(resetFlagQuery);
				q.executeUpdate();
			}
		}catch(Exception ex){
			logger.error(ex, "Error orccured while resetting flags before step execution");
		}
	}

	@Override
	@Transactional 
	public ExitStatus afterStep(StepExecution stepExecution) {
		try{
			
			logger.info("Number of rows read: " + stepExecution.getReadCount()); 
			if(stepExecution.getReadCount() > 0){
			
				logger.info("Performing cleanup and deactivation of suppliers.");
				int ncvCount = 0;
				int cvCount = 0;
				Query query = null;
				if(make.equalsIgnoreCase("nissan")){
					String recordCountQuery = "SELECT " +
												"  (SELECT COUNT(1) " +
												"     FROM willow2k.potential_supplier " +
												"    WHERE (NVL(ncv_batch, 'N') = 'Y' " +
												"      AND NVL(cv_batch, 'N')     = 'N') " +
												"  ) ncv_count, " +
												"  (SELECT COUNT(1)" +
												"     FROM willow2k.potential_supplier " +
												"    WHERE (NVL(ncv_batch, 'N') = 'N' " +
												"      AND NVL(cv_batch, 'N')     = 'Y') " +
												"  ) cv_count  " +
											"	FROM dual ";
	
					
					query = entityManager.createNativeQuery(recordCountQuery);
					@SuppressWarnings("unchecked")
					List<Object[]> resultList = (List<Object[]>)query.getResultList();
					if(!resultList.isEmpty()){
						ncvCount = ((BigDecimal)resultList.get(0)[0]).intValue();
						cvCount = ((BigDecimal)resultList.get(0)[1]).intValue();
					}
				}else{
					ncvCount = 1;
					cvCount = 1;
				}
				
				String deleteQueryString =  null;
				if(ncvCount > 0 && cvCount > 0){
					deleteQueryString = "DELETE FROM SUPPLIER_WORKSHOPS WHERE WORKSHOP_CAPABILITY= 'CD_DEALER' AND sup_sup_id IN ( "
							+ " SELECT distinct(sup_id) "
							+ "   FROM suppliers s, supplier_franchises sf "
							+ "  WHERE S.SUP_ID = sf.sup_sup_id "
							+ "    AND s.sup_id = sf.sup_sup_id "
							+ "    AND MAK_id in (select mak_id from makes where lower(make_desc) = '" + make.toLowerCase() + "' )"
							+ "    AND NOT EXISTS (SELECT 1 FROM WILLOW2K.potential_supplier WHERE  mak_mak_id = "+parentProviderId+" and ltrim(trim(make_code) , '0')  = ltrim(trim(sf.make_code) , '0')  and (NVL(ncv_batch, 'Y') = 'Y' OR NVL(cv_batch, 'Y') = 'Y')) " 
							+ "  )";
					
					query = entityManager.createNativeQuery(deleteQueryString);
					query.executeUpdate();
					
					deleteQueryString = "DELETE FROM WILLOW2K.potential_supplier WHERE mak_mak_id = " + parentProviderId + " AND (supplier_exist_yn = 'Y' OR (ncv_batch = 'N' AND cv_batch = 'N'))";
					
				} else{
					deleteQueryString = "DELETE FROM WILLOW2K.potential_supplier WHERE mak_mak_id = " + parentProviderId + " AND ncv_batch = 'N' AND cv_batch = 'N'";
				}
				
				query = entityManager.createNativeQuery(deleteQueryString);
				query.executeUpdate();
			}
		}catch(Exception ex){
			logger.error(ex, "Error occurred while performing on job completion task.");
		}
		return ExitStatus.COMPLETED;
	}
	

}
