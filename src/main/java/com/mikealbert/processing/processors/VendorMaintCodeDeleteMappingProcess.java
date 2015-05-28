package com.mikealbert.processing.processors;

import java.util.Map;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.exception.MalBusinessException;

@Component("vendorMaintCodeDeleteMappingProcess")
public class VendorMaintCodeDeleteMappingProcess {
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public ServiceProviderMaintenanceCode mapStoreLocation(StoreLocationVO storeLocation, @Headers Map<String, Object> headers) throws MalBusinessException{
		// TODO: what validation should be here?
		// we should do mapping validation (prior to this)
		// should we have a format validation prior to this step
		
		ServiceProviderMaintenanceCode maintCode = null;
		
		logger.info("Getting ready to add vendor maint code : " + maintCode.getCode());
		
		return maintCode;
	}
}
