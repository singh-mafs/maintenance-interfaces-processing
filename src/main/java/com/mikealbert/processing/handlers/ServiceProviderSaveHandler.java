package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.ServiceProviderService;

@Component("serviceProviderSaveHandler")
public class ServiceProviderSaveHandler {
	
	@Resource ServiceProviderService serviceProviderService;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public void save(ServiceProvider req) throws MalBusinessException{
		ServiceProvider retVal = null;
		
		logger.info("Service Provider - " + req.getServiceProviderNumber() + " with name " + req.getServiceProviderName() + " is being added" + " for provider - " + req.getParentServiceProvider().getServiceProviderName() );

		retVal = serviceProviderService.saveServiceProvider(req);

		logger.info("Service Provider - " + retVal.getServiceProviderId() + " with name " + retVal.getServiceProviderName() + " and code " + retVal.getServiceProviderNumber() + " was saved for provider - " + req.getParentServiceProvider().getServiceProviderName() );
	}
}
