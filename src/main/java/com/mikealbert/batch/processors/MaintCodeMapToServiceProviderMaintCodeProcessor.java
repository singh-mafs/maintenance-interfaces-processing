package com.mikealbert.batch.processors;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.mikealbert.batch.listeners.StepExecutionListenerCtxInjector;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.MaintenanceCode;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.data.vo.MaintCodeMappingVO;
import com.mikealbert.exception.MalException;
import com.mikealbert.service.MaintenanceCodeService;
import com.mikealbert.service.ServiceProviderService;
import com.mikealbert.util.MALUtilities;

public class MaintCodeMapToServiceProviderMaintCodeProcessor<T> implements ItemProcessor<MaintCodeMappingVO, ServiceProviderMaintenanceCode> {
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Resource StepExecutionListenerCtxInjector ctxInjector;
	@Resource ServiceProviderService providerService;
	@Resource MaintenanceCodeService maintCodeService;
	
	private Long parentProviderId;
	
	@Value("#{jobParameters[parentProviderId]}")
	public void setParentProviderId(Long parentProviderId) {
		this.parentProviderId = parentProviderId;
	}
	
	@Override
	public ServiceProviderMaintenanceCode process(MaintCodeMappingVO maintCodeMapping) throws Exception {
		//TODO: how do we want to handle errors?
		
		ServiceProviderMaintenanceCode providerMaintCode = new ServiceProviderMaintenanceCode();
		ServiceProvider parentProvider = providerService.getServiceProvider(parentProviderId);
		providerMaintCode.setServiceProvider(parentProvider);
		
		
		// check to see if that code has already been added
		if(maintCodeService.isServiceProviderCodeAdded(maintCodeMapping.getVendorMaintCode(), parentProviderId, false)){
			throw new MalException("generic.error", 
					new String[] { "Vendor Code - " + maintCodeMapping.getVendorMaintCode() + " for Provider " + parentProvider.getServiceProviderName()  + " already exists " });
		}
		
		MaintenanceCode maintCode = maintCodeService.getExactMaintenanceCodeByNameOrCode(maintCodeMapping.getMafsMaintCode());
		
		// handle an incorrect or missing MAFS code
		if(!MALUtilities.isEmpty(maintCode)){
			providerMaintCode.setMaintenanceCode(maintCode);
		}else{ 		
			
			throw new MalException("generic.error", 
				new String[] { "This is no MAFS Maintenance Code for : " + maintCodeMapping.getMafsMaintCode()});
		}
		
		providerMaintCode.setCode(maintCodeMapping.getVendorMaintCode().toUpperCase());
		providerMaintCode.setDescription(maintCodeMapping.getVendorDescription());
		
		// set things up to auto-approve
		providerMaintCode.setApprovedBy("AUTO");
		providerMaintCode.setApprovedDate(new Date());
		
		logger.info("Getting ready to add vendor maint code : " + providerMaintCode.getCode());
		
		return providerMaintCode;
	}
}
