package com.mikealbert.processing.processors;

import java.util.ArrayList;
import java.util.List;
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
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.data.vo.VendorMaintCodeVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.language.util.LanguageUtils;
import com.mikealbert.language.util.RegExpMatchTokenizer;
import com.mikealbert.service.LookupCacheService;
import com.mikealbert.service.MaintenanceCodeService;

@Component("vendorMaintCodeUpdateMappingProcess")
public class VendorMaintCodeUpdateMappingProcess {
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	// TODO: move the helper code around
	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	@Resource LookupCacheService lookupService;
	@Resource MaintenanceCodeService maintCodeService;
	private double minConfidence = 0.9;

	@Handler
	public ServiceProviderMaintenanceCode mapVendorMaintCode(VendorMaintCodeVO vendorMaintCode, @Headers Map<String, Object> headers) throws MalBusinessException{
		// TODO: what validation should be here?
		// we should do mapping validation (prior to this)
		// should we have a format validation prior to this step
		
		//TODO: move helper code around
		ServiceProvider provider = storeToProviderMappingHelper.getParentFromProperties(headers);
		List<Long> providerIds = new ArrayList<Long>();
		providerIds.add(provider.getServiceProviderId());
		
		List<ServiceProviderMaintenanceCode> vendorMaintCodes = maintCodeService.getServiceProviderMaintenanceCode(vendorMaintCode.getPartServiceCode(), providerIds,false);
		
		ServiceProviderMaintenanceCode vendorCode = vendorMaintCodes.get(0);
		vendorCode.setDescription(vendorMaintCode.getPartServiceDesc());
		vendorCode.setApprovedBy(null);
		vendorCode.setApprovedDate(null);
		
		logger.info("Getting ready to update vendor maint code : " + vendorCode.getCode());
		
		return vendorCode;
	}
	
	private MaintenanceCode findMatchingMaintCode(String partServiceDesc){
		MaintenanceCode returnCode = null;
		LanguageUtils lang = new LanguageUtils();
		RegExpMatchTokenizer tokenizer = new RegExpMatchTokenizer("[A-z]+",true);
		lang.setTokenizer(tokenizer);
		
		// TODO: we need to decide how we are going to clear cache and how often.
		List<MaintenanceCode> maintCodes =  lookupService.getMaintenanceCodes();
		//TODO: we need to find the "best" confidence rating, not just the first one that is over the minConfidence
		for(MaintenanceCode maintCode: maintCodes){
			if(lang.jaccardSimilarity(partServiceDesc, maintCode.getDescription()) >=minConfidence){
				returnCode = maintCode;
				break;
			}
		}
		
		return returnCode;
	}
	
}
