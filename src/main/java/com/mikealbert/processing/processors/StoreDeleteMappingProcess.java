package com.mikealbert.processing.processors;

import java.util.Map;
import javax.annotation.Resource;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalConstants;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.AddressService;
import com.mikealbert.service.ServiceProviderService;

@Component("storeDeleteMappingProcess")
public class StoreDeleteMappingProcess {
	
	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	@Resource ServiceProviderService serviceProviderService;
	@Resource AddressService addressService;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public ServiceProvider mapStoreLocation(StoreLocationVO storeLocation, @Headers Map<String, Object> headers) throws MalBusinessException{
		// lookup the provider off of their parent id and store no		
		ServiceProvider parent = storeToProviderMappingHelper.getParentFromProperties(headers);
		//TODO: error handling for a "not found" store number
		ServiceProvider provider = serviceProviderService.getServiceProviderByProviderCode(storeLocation.getStoreCode(),parent);
		
		provider.setInactiveInd(MalConstants.FLAG_Y);
		provider.setEnteredBy(StoreToProviderMappingHelper.AUTO_EMPLOYEE_NO);
		
		logger.info("Getting ready to logically delete store/provider : " + provider.getServiceProviderNumber());
		
		return provider;
	}
}
