package com.mikealbert.processing.processors;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalConstants;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderAddress;
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.AddressService;
import com.mikealbert.service.ServiceProviderService;

@Component("storeAddMappingProcess")
public class StoreAddMappingProcess {

	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	@Resource ServiceProviderService serviceProviderService;
	@Resource AddressService addressService;

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public ServiceProvider mapStoreLocation(StoreLocationVO storeLocation, @Headers Map<String, Object> headers) throws MalBusinessException{
		// TODO: what validation should be here?
		// we should do mapping validation (prior to this)
		// should we have a format validation prior to this step
		
		ServiceProvider provider = new ServiceProvider();
		ServiceProvider parent = storeToProviderMappingHelper.getParentFromProperties(headers);
		// map attributes that are being set directly from the parent
		storeToProviderMappingHelper.mapParentAttributes(provider, parent);
		// map common attributes
		storeToProviderMappingHelper.mapCommonAttributes(provider, storeLocation);
		// map (and copy) the discounts from the parent
		storeToProviderMappingHelper.mapDiscounts(provider, parent);
		
		// set entered by
		provider.setEnteredBy(StoreToProviderMappingHelper.AUTO_EMPLOYEE_NO);
		provider.setLastUpdateDate(new Date());
		
		// create and map a POST address as the default address
		ServiceProviderAddress postAddress = new ServiceProviderAddress();
		List<ServiceProviderAddress> addresses = new ArrayList<ServiceProviderAddress>();
		postAddress.setAddressType(StoreToProviderMappingHelper.POST_ADDRESS_TYPE);
		postAddress.setDefaultInd(MalConstants.FLAG_Y);
		postAddress.setServiceProvider(provider);
		storeToProviderMappingHelper.mapAddressAttributes(postAddress, parent, storeLocation);
		addresses.add(postAddress);
		provider.setServiceProviderAddresses(addresses);
		
		logger.info("Getting ready to add store/provider : " + provider.getServiceProviderNumber());
		
		return provider;
	}
}
