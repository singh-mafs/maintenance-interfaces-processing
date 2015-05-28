package com.mikealbert.data.util;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.MaintenanceCode;
import com.mikealbert.language.util.LanguageUtils;
import com.mikealbert.language.util.StemRegExpMatchTokenizer;
import com.mikealbert.service.LookupCacheService;

@Component("maintCodeMappingHelper")
public class MaintCodeMappingHelper {
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	@Resource LookupCacheService lookupService;
	private double minConfidence = 0.3;
	
	public MaintenanceCode findMatchingMaintCode(String partServiceDesc){
		double bestMatchConfidence = 0d;
		double currMatchConfidence = 0d;
		MaintenanceCode bestMatchInstance = null;

		LanguageUtils lang = new LanguageUtils();
		StemRegExpMatchTokenizer tokenizer = new StemRegExpMatchTokenizer("[A-z/]+",true);
		lang.setTokenizer(tokenizer);
		
		// TODO: we need to decide how we are going to clear cache and how often.
		List<MaintenanceCode> maintCodes =  lookupService.getMaintenanceCodes();
		//we need to find the "best" confidence rating, not just the first one that is over the minConfidence
		for(MaintenanceCode maintCode: maintCodes){
			currMatchConfidence = lang.jaccardSimilarity(partServiceDesc, maintCode.getDescription());
			if(currMatchConfidence >=minConfidence){
				if(currMatchConfidence > bestMatchConfidence){
					bestMatchConfidence = currMatchConfidence;
					bestMatchInstance = maintCode;
				}
			}
		}
		if(bestMatchInstance != null){
			logger.info("Matched Vendor Code Desc : " + partServiceDesc + " = " + bestMatchInstance.getDescription() + " : " + bestMatchInstance.getCode());
		}else{
			logger.info("Did not match Vendor Code Desc : " + partServiceDesc);
		}
		
		return bestMatchInstance;
	}

}
