package com.mikealbert.batch.processors;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.mikealbert.batch.listeners.StepExecutionListenerCtxInjector;
import com.mikealbert.batch.mappers.DeliveringDealerMapper;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.exception.MalException;
import com.mikealbert.service.ServiceProviderService;
import com.mikealbert.util.MALUtilities;

public class DeliveringDealerProcessor<T> implements ItemProcessor<DeliveringDealerMapper, DeliveringDealerMapper> {

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	private static final int MAKE_CODE_DB_COLUMN_LEN = 20;
	private static final int AREA_DB_COLUMN_LEN = 20;
	private static final int AREA_DESC_DB_COLUMN_LEN = 200;
	private static final int SUPPLIER_NAME_DB_COLUMN_LEN = 60;
	private static final int PHONE_DB_COLUMN_LEN = 25;
	private static final int ADDRESS_DB_COLUMN_LEN = 200;
	private static final int TOWN_DB_COLUMN_LEN = 25;
	private static final int REGION_DB_COLUMN_LEN = 80;
	private static final int POSTCODE_DB_COLUMN_LEN = 25;
	private static final int COUNTRY_DB_COLUMN_LEN = 80;
	private static final int CONTACT_NAME_DB_COLUMN_LEN = 100;
	private static final int CONTACT_PHONE_COLUMN_LEN = 25;
	private static final int CONTACT_EMAIL_COLUMN_LEN = 100;
	private static final int FAX_COLUMN_LEN = 80;
	private static final int FILE_NAME_DB_COLUMN_LEN = 100;
	
	@Resource StepExecutionListenerCtxInjector ctxInjector;
	@Resource ServiceProviderService serviceProviderService;
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	private String make, inputResource;
	private Long parentProviderId;
	private String folderName;
	
	@Value("#{jobParameters[inputResource]}")
	public void setInputResource(String inputResource) {
		this.inputResource = inputResource;
	}
	
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
	public DeliveringDealerMapper process(DeliveringDealerMapper deliveringDealerObj) throws Exception {
		
		if(MALUtilities.isEmpty(deliveringDealerObj.getMakeCode()) 
				|| MALUtilities.isEmpty(deliveringDealerObj.getDealerName())
				|| MALUtilities.isEmpty(deliveringDealerObj.getAddress())
				|| MALUtilities.isEmpty(deliveringDealerObj.getCity())
				|| MALUtilities.isEmpty(deliveringDealerObj.getState())
				|| MALUtilities.isEmpty(deliveringDealerObj.getZip())){
			throw new MalException("Make code, Dealer Name, Address, City, State and Postal Code cannot be blank. Record: " + deliveringDealerObj.toString());
		}
		
		try{
			List<Object> supplierList = serviceProviderService.getSupplierByMakeCodeOrName(deliveringDealerObj.getMakeCode(), make);
			
			if(supplierList == null || supplierList.isEmpty()){
				deliveringDealerObj.setSupplierExistsYN("N");
			}else{
				deliveringDealerObj.setSupplierExistsYN("Y");
			}
			deliveringDealerObj.setMakeId(parentProviderId);
			
			if(make.equalsIgnoreCase("nissan")){
				if(folderName.toLowerCase().contains("ncv")){
					deliveringDealerObj.setNcvBatchYN("Y");
					deliveringDealerObj.setCvBatchYN("N");
				}else{
					deliveringDealerObj.setCvBatchYN("Y");
					deliveringDealerObj.setNcvBatchYN("N");
				}
			}
			
			deliveringDealerObj.setProcessedFileName(inputResource.substring(inputResource.lastIndexOf("\\") + 1, inputResource.length()));
			
			
			deliveringDealerObj.setMakeCode(deliveringDealerObj.getMakeCode().substring(0, Math.min(deliveringDealerObj.getMakeCode().length(), MAKE_CODE_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getArea()))
				deliveringDealerObj.setArea(deliveringDealerObj.getArea().substring(0, Math.min(deliveringDealerObj.getArea().length(), AREA_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getAreaDescription()))
				deliveringDealerObj.setAreaDescription(deliveringDealerObj.getAreaDescription().substring(0, Math.min(deliveringDealerObj.getAreaDescription().length(), AREA_DESC_DB_COLUMN_LEN)  ));
			
			deliveringDealerObj.setDealerName(deliveringDealerObj.getDealerName().substring(0, Math.min(deliveringDealerObj.getDealerName().length(), SUPPLIER_NAME_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getDealerPhone()))
				deliveringDealerObj.setDealerPhone(deliveringDealerObj.getDealerPhone().substring(0, Math.min(deliveringDealerObj.getDealerPhone().length(), PHONE_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getAddress()))
				deliveringDealerObj.setAddress(deliveringDealerObj.getAddress().substring(0, Math.min(deliveringDealerObj.getAddress().length(), ADDRESS_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getCity()))
				deliveringDealerObj.setCity(deliveringDealerObj.getCity().substring(0, Math.min(deliveringDealerObj.getCity().length(), TOWN_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getRegion()))
				deliveringDealerObj.setRegion(deliveringDealerObj.getRegion().substring(0, Math.min(deliveringDealerObj.getRegion().length(), REGION_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getZip()))
				deliveringDealerObj.setZip(deliveringDealerObj.getZip().substring(0, Math.min(deliveringDealerObj.getZip().length(), POSTCODE_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getContactName()))
				deliveringDealerObj.setContactName(deliveringDealerObj.getContactName().substring(0, Math.min(deliveringDealerObj.getContactName().length(), CONTACT_NAME_DB_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getTedContactPhone()))
				deliveringDealerObj.setTedContactPhone(deliveringDealerObj.getTedContactPhone().substring(0, Math.min(deliveringDealerObj.getTedContactPhone().length(), CONTACT_PHONE_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getFax()))
				deliveringDealerObj.setFax(deliveringDealerObj.getFax().substring(0, Math.min(deliveringDealerObj.getFax().length(), FAX_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getEmail()))
				deliveringDealerObj.setEmail(deliveringDealerObj.getEmail().substring(0, Math.min(deliveringDealerObj.getEmail().length(), CONTACT_EMAIL_COLUMN_LEN)  ));
			
			if(!MALUtilities.isEmpty(deliveringDealerObj.getProcessedFileName()))
				deliveringDealerObj.setProcessedFileName(deliveringDealerObj.getProcessedFileName().substring(0, Math.min(deliveringDealerObj.getProcessedFileName().length(), FILE_NAME_DB_COLUMN_LEN)  ));
			
			String stagedSupplierCountQuery = "SELECT ps_id, address, town_city, region, postcode from WILLOW2K.potential_supplier where make_code = :code and mak_mak_id = :mak_Id";
			
			Query query = entityManager.createNativeQuery(stagedSupplierCountQuery);
			query.setParameter("code", deliveringDealerObj.getMakeCode());
			query.setParameter("mak_Id", parentProviderId);
			
			List<Object[]> result = query.getResultList();
			
			if(!result.isEmpty()){
				
				StringBuilder sqlStmt = new StringBuilder("");
				String resetLongitudeLatitudeQuery = null;
				sqlStmt.append("UPDATE WILLOW2K.potential_supplier SET area = :area, area_description = :areaDescription, supplier_name = :name, telephone_number = :phone, ");
				sqlStmt.append("	contact_name = :contactName, contact_phone = :contactPhone, contact_email = :contactEmail, fax_number = :fax, ");
				
				if(!(deliveringDealerObj.getAddress().equalsIgnoreCase((String)(result.get(0)[1])) 
						&& deliveringDealerObj.getCity().equalsIgnoreCase((String)(result.get(0)[2]))
						&& deliveringDealerObj.getState().equalsIgnoreCase((String)(result.get(0)[3]))
						&& deliveringDealerObj.getZip().equalsIgnoreCase((String)(result.get(0)[4])))){
					
					sqlStmt.append("	address= :address, town_city = :city, region = :state, postcode = :zip, " );
					
					resetLongitudeLatitudeQuery = "DELETE FROM willow2k.potential_supplier_geolocation WHERE ps_ps_id = :psId";
				}
				
				sqlStmt.append(" processed_file_name = :fileName, ncv_batch = :ncvBatchYN , cv_batch = :cvBatchYN, versionts = sysdate where make_code = :makeCode and mak_mak_id = :makeId");
				
				query = entityManager.createNativeQuery(sqlStmt.toString());

				query.setParameter("ncvBatchYN", deliveringDealerObj.getNcvBatchYN());
				query.setParameter("cvBatchYN", deliveringDealerObj.getCvBatchYN());
				query.setParameter("makeCode", deliveringDealerObj.getMakeCode());
				query.setParameter("makeId", parentProviderId);
				
				query.setParameter("area", deliveringDealerObj.getArea());
				query.setParameter("areaDescription", deliveringDealerObj.getAreaDescription());
				query.setParameter("name", deliveringDealerObj.getDealerName());
				query.setParameter("phone", deliveringDealerObj.getDealerPhone());
				query.setParameter("fax", deliveringDealerObj.getFax());

				if(!(deliveringDealerObj.getAddress().equalsIgnoreCase((String)(result.get(0)[1])) 
						&& deliveringDealerObj.getCity().equalsIgnoreCase((String)(result.get(0)[2]))
						&& deliveringDealerObj.getState().equalsIgnoreCase((String)(result.get(0)[3]))
						&& deliveringDealerObj.getZip().equalsIgnoreCase((String)(result.get(0)[4])))){
					
					query.setParameter("address", deliveringDealerObj.getAddress());
					query.setParameter("city", deliveringDealerObj.getCity());
					query.setParameter("state", deliveringDealerObj.getState());
					query.setParameter("zip", deliveringDealerObj.getZip());
					
				}
				
				query.setParameter("contactName", deliveringDealerObj.getContactName());
				query.setParameter("contactPhone", deliveringDealerObj.getTedContactPhone());
				query.setParameter("contactEmail", deliveringDealerObj.getEmail());
				query.setParameter("fileName", deliveringDealerObj.getProcessedFileName());
			
				query.executeUpdate();
				
				if(resetLongitudeLatitudeQuery != null){
					try{
						query = entityManager.createNativeQuery(resetLongitudeLatitudeQuery);
						query.setParameter("psId", (BigDecimal)result.get(0)[0]);
						query.executeUpdate();
					}catch(Exception ex){
						logger.error(ex, "Error occurred while processing code: " + deliveringDealerObj.getMakeCode() + " . Unable to reset longitude latitude.");
					}
				}
				
				if(MALUtilities.convertYNToBoolean(deliveringDealerObj.getSupplierExistsYN())){
					query = entityManager.createNativeQuery("Update suppliers set inactive_ind= 'N', versionts = sysdate where sup_id in "
														+ "(select distinct(sup_sup_id) from supplier_franchises where make_code = '"+ deliveringDealerObj.getMakeCode() + "' and mak_id = " + parentProviderId + ")");
					query.executeUpdate();
				}
				return null; // do not insert already existing row.
			}
			
		
		}catch(Exception ex){
			logger.error(ex, "Error occurred while processing code: " + deliveringDealerObj.getMakeCode());
		}
		return deliveringDealerObj;
	}
}
