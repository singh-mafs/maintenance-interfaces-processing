package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.MaintenanceRequest;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.MaintenanceRequestService;

@Component("maintenanceRequestSaveHandler")
public class MaintenanceRequestSaveHandler {
	
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
			fromDb.setMaintReqStatus(req.getMaintReqStatus());
			maintRequestService.saveOrUpdateMaintnenacePO(fromDb, req.getLastChangedBy());
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
