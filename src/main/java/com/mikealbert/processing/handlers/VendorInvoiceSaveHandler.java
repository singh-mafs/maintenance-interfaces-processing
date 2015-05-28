package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProviderInvoiceHeader;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.ServiceProviderService;

@Component("vendorInvoiceSaveHandler")
public class VendorInvoiceSaveHandler {
	
	@Resource ServiceProviderService providerService;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public void save(ServiceProviderInvoiceHeader req) throws MalBusinessException{
		logger.info("Vendor Invoice - " + req.getRecordId() + " for Po/Job Nbr " + req.getJobNo() + " is being added ");

		providerService.saveServiceProviderInvoice(req);
		
		providerService.processServiceProviderInvoices(req.getLoadId(), 1L, "DUNCAN");

		logger.info("Vendor Invoice - " + req.getRecordId() + " for Po/Job Nbr " + req.getJobNo() + " was added ");
	}
}
