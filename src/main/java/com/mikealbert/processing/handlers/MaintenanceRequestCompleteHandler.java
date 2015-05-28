package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.MaintenanceRequest;
import com.mikealbert.data.enumeration.CorporateEntity;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.MaintenanceRequestService;
import com.mikealbert.util.MALUtilities;

@Component("maintenanceRequestCompleteHandler")
public class MaintenanceRequestCompleteHandler {
	
	@Resource MaintenanceRequestService maintRequestService;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	@Handler
	public void save(MaintenanceRequest req){
		try {
			logger.info("Maintenance Request - " + req.getJobNo() + " with status " + req.getMaintReqStatus() + " is being auto-updated");
			// We need a handler and a lookup because 
			// versionTS cannot be serialized as a message
			// because it is not part of the public interface to this object (it is always null)
			MaintenanceRequest fromDb = maintRequestService.getMaintenanceRequestByMrqId(req.getMrqId());
			// fromDb.getServiceProvider().getServiceProviderId()
			// find the Corporate Entity associated with the Payee Account, this will be used as the CID along with the user for validation when completing the PO.
			//TODO: find out why Payee Account is sometimes null
			CorporateEntity selectedEntity = null;
			for(CorporateEntity ent : CorporateEntity.values()){
				if(!MALUtilities.isEmpty(fromDb.getPayeeAccount()) && ent.getCorpId() == fromDb.getPayeeAccount().getExternalAccountPK().getCId()){
					selectedEntity = ent;
					break;
				}else{
					selectedEntity = CorporateEntity.MAL;
				}
			}
			maintRequestService.completeMRQ(fromDb,selectedEntity, req.getLastChangedBy());
			// read back the updated PO
			fromDb = maintRequestService.getMaintenanceRequestByMrqId(req.getMrqId());
			// pass it back to update the body
			req = fromDb;
			
		} catch (MalBusinessException e) {
			logger.error(e, "Maintenance Request - " + req.getJobNo() + " with status " + req.getMaintReqStatus() + " failed update");
		} catch (Exception e) {
			logger.error(e, "Maintenance Request - " + req.getJobNo() + " with status " + req.getMaintReqStatus() + " failed update");
		}
		
		
		
	}
}
