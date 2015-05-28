package com.mikealbert.processing.processors;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderAddress;
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.AddressService;
import com.mikealbert.service.ServiceProviderService;
import com.mikealbert.util.MALUtilities;

@Component("storeUpdateMappingProcess")
public class StoreUpdateMappingProcess {
	
	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	@Resource ServiceProviderService serviceProviderService;
	@Resource AddressService addressService;

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	@Handler
	public ServiceProvider mapStoreLocation(StoreLocationVO storeLocation,  @Headers Map<String, Object> headers) throws MalBusinessException{
		// lookup the provider off of their parent id and store no		
		ServiceProvider parent = storeToProviderMappingHelper.getParentFromProperties(headers);
		//error handling for a "not found" store number
		ServiceProvider provider = serviceProviderService.getServiceProviderByProviderCode(storeToProviderMappingHelper.reformatStoreCode(storeLocation.getStoreCode()),parent);
		if(MALUtilities.isEmpty(provider)){
			throw new MalBusinessException("A Service Provider does not exist for : " + storeLocation.getStoreCode() + " , the record cannot be process with operation code M");
		}
		// map common attributes (re-map to update them)
		storeToProviderMappingHelper.mapCommonAttributes(provider, storeLocation);
		
		// select the POST address and send it in for re-mapping (to update it)
		ServiceProviderAddress postAddress = this.getPostalAddress(provider.getServiceProviderAddresses());
		storeToProviderMappingHelper.mapAddressAttributes(postAddress, parent, storeLocation);
		
		logger.info("Getting ready to update store/provider : " + provider.getServiceProviderNumber());
		
		return provider;
	}
	
	private ServiceProviderAddress getPostalAddress(List<ServiceProviderAddress> serviceProviderAddresses){
		ServiceProviderAddress postAddress = null;
	
		for(ServiceProviderAddress addr : serviceProviderAddresses){
			if(addr.getAddressType().equalsIgnoreCase(StoreToProviderMappingHelper.POST_ADDRESS_TYPE)){
				postAddress = addr;
				break;
			}
		}
		
		return postAddress;
	}
}
