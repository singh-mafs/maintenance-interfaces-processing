package com.mikealbert.processing.processors;

import java.util.Map;

import javax.annotation.Resource;



import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.MaintenanceCode;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.data.util.MaintCodeMappingHelper;
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.VendorMaintCodeVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.util.MALUtilities;


@Component("vendorMaintCodeAddMappingProcess")
public class VendorMaintCodeAddMappingProcess {

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	@Resource MaintCodeMappingHelper maintCodeMappingHelper;
	
	@Handler
	public ServiceProviderMaintenanceCode mapVendorMaintCode(VendorMaintCodeVO vendorMaintCode, @Headers Map<String, Object> headers) throws MalBusinessException{
		// TODO: what validation should be here?
		// we should do mapping validation (prior to this)
		// should we have a format validation prior to this step
		
		//TODO: move helper code around
		ServiceProvider provider = storeToProviderMappingHelper.getParentFromProperties(headers);
		
		ServiceProviderMaintenanceCode vendorCode = new ServiceProviderMaintenanceCode();
		if (MALUtilities.isNotEmptyString(vendorMaintCode.getPartServiceCode()))
			vendorCode.setCode(vendorMaintCode.getPartServiceCode().toUpperCase());
		vendorCode.setDescription(vendorMaintCode.getPartServiceDesc());
		MaintenanceCode maintCode = maintCodeMappingHelper.findMatchingMaintCode(vendorMaintCode.getPartServiceDesc());
		vendorCode.setMaintenanceCode(maintCode);
		vendorCode.setServiceProvider(provider);
		
		logger.info("Getting ready to add vendor maint code : " + vendorCode.getCode());
		
		return vendorCode;
	}
}
