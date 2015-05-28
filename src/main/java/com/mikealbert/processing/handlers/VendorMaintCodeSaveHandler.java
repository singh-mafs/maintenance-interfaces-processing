package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.exception.MalBusinessException;
//import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.MaintenanceCodeService;

@Component("vendorMaintCodeSaveHandler")
public class VendorMaintCodeSaveHandler {
	
	@Resource MaintenanceCodeService maintCodeService;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public void save(ServiceProviderMaintenanceCode req) throws MalBusinessException{
		ServiceProviderMaintenanceCode retVal =  null;
		logger.info("Vendor Maintenance Code - " + req.getCode() + " with description " + req.getDescription() + " is being added" + " for provider - " + req.getServiceProvider().getServiceProviderName() );

		retVal = maintCodeService.saveServiceProviderMaintCode(req);

		logger.info("Vendor Maintenance Code - " + retVal.getSmlId() + " with description " + retVal.getDescription() + " and code " + retVal.getCode() + " was saved for provider - " + req.getServiceProvider().getServiceProviderName() );
	}
}
