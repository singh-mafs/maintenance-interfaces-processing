package com.mikealbert.processing.processors;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.DynamicRouter;
import org.apache.camel.Headers;
import org.apache.camel.Properties;
import org.springframework.beans.factory.annotation.Value;

import com.mikealbert.exception.MalException;
import com.mikealbert.util.MALUtilities;

public class ConfigurableFileRoutingProcess {
	private static final String IDEAL_STORE_LOC = "storeLocationJob";
	private static final String IDEAL_VENDOR_MAINT_CODE = "vendorMaintCodeJob";
	private static final String IDEAL_VENDOR_INVOICE = "vendorInvoiceJob";
	private static final String IDEAL_DIRECT_MAP_CODES = "directMapMaintCodesJob";
	
	private static final int LOCS_JOB_IDX = 0;
	private static final int CODES_JOB_IDX = 1;
	private static final int INVOICES_JOB_IDX = 2;
	private static final int MAPPING_JOB_IDX = 3;
	
	@Resource Map<String, List<String>> providerFileToProcessingJobMap;

	@Value("${files.input.delivering.dealer}")
	private String deliveringDealerFolder; 

	@Value("${process.locationsAndCodes}")
	private boolean processLocationsAndCodes; 

	
//	@DynamicRouter
	public String determineRoute(@Headers Map<String, Object> headers, @Properties Map<String, Object> properties){
		// if we have already routed this request (storeLocationVO) skip it
		if(getInvokedMarker(properties)){
			return null;
		}
		setInvokedMarker(properties,true);
		return resolveHeadersToRoute(headers);
	}
	
	private void setInvokedMarker(@Properties Map<String, Object> properties, boolean invoked){
	    // store the "invoked" maker back on the properties to trigger to not invoke again
	    properties.put("invoked", invoked);
	}
	
	private boolean getInvokedMarker(@Properties Map<String, Object> properties){
	    // get the state from the exchange properties and keep track how many times
	    // we have been invoked
	    boolean invoked = false;
	    Object current = properties.get("invoked");
	    if (current != null) {
	        invoked = Boolean.valueOf(current.toString());
	    }

	    return invoked;
	}
	
	private String resolveHeadersToRoute(@Headers Map<String, Object> headers){
		// for a supplier
		String parentProviderNumber = headers.get("parentProviderNumber").toString();
		List<String> targetJobs =  providerFileToProcessingJobMap.get(parentProviderNumber);
		String targetJob;
		String inputResourceUpper = headers.get("inputResource").toString().toUpperCase();
		String targetRoute = "";		
		String fileName = inputResourceUpper.substring(inputResourceUpper.lastIndexOf("\\") + 1, inputResourceUpper.length());
		
		
		// DeliverigDealer Job execution
		if(inputResourceUpper.contains(deliveringDealerFolder.toUpperCase())){
			String folderName = headers.get("folderName").toString().toUpperCase();
			
			if(folderName.contains("DELIVERING") 
					&& folderName.contains("NISSAN") 
					&& folderName.contains("NCV")
					&& fileName.contains("NISSAN")
					&& fileName.contains("NCV")
					&& fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length()).equalsIgnoreCase("CSV")){
				targetRoute = "spring-batch:" + "nissanDeliveringDealerNcvJob";
			}
			else if(folderName.contains("DELIVERING") && folderName.contains("NISSAN") && !folderName.contains("NCV")
					&& fileName.contains("NISSAN")
					&& !fileName.contains("NCV")
					&& fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length()).equalsIgnoreCase("CSV")){
				targetRoute = "spring-batch:" + "nissanDeliveringDealerJob";
			}else if(folderName.contains("DELIVERING") && folderName.contains("TOYOTA")
					&& fileName.contains("TOYOTA") 
					&& fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length()).equalsIgnoreCase("CSV")){
				targetRoute = "spring-batch:" + "deliveringDealerJob";
			}
			if(!MALUtilities.isEmpty(targetRoute))
				return targetRoute;
			else
				throw new MalException("generic.error", 
						new String[] { "The File/Resources name does not match to a high level route pattern of DELIVERING, TOYOTA, NISSAN, NCV  : " + inputResourceUpper});
		}
		
		// for a file type (derived from the file/path name)
		if((inputResourceUpper.contains("STORE") || inputResourceUpper.contains("LOCAT")) && processLocationsAndCodes){
			// send to a target route (stored externally)
			targetJob = findJobNameByIndex(LOCS_JOB_IDX,targetJobs);
			if(MALUtilities.isNotEmptyString(targetJob)){
				targetRoute = "spring-batch:" + targetJob;
			}else{
				throw new MalException("generic.error", 
					new String[] { "There are no processing jobs mapped for the Store Locations of Provider Number : " + parentProviderNumber});
			}
		}else if(inputResourceUpper.contains("MAP") && processLocationsAndCodes){
			// send to a target route (stored externally)
			targetJob = findJobNameByIndex(MAPPING_JOB_IDX,targetJobs);
			if(MALUtilities.isNotEmptyString(targetJob)){
				targetRoute = "spring-batch:" + targetJob;
			}else{
				throw new MalException("generic.error", 
					new String[] { "There are no processing jobs mapped for the Maintenance Codes of Provider Number : " + parentProviderNumber});
			}
		}else if(inputResourceUpper.contains("CODE") && processLocationsAndCodes){
			// send to a target route (stored externally)
			targetJob = findJobNameByIndex(CODES_JOB_IDX,targetJobs);
			if(MALUtilities.isNotEmptyString(targetJob)){
				targetRoute = "spring-batch:" + targetJob;
			}else{
				throw new MalException("generic.error", 
					new String[] { "There are no processing jobs mapped for the Maintenance Codes of Provider Number : " + parentProviderNumber});
			}
		}else if(inputResourceUpper.contains("INVOICE") || inputResourceUpper.contains("BILL")){
			// send to a target route (stored externally)
			targetJob = findJobNameByIndex(INVOICES_JOB_IDX,targetJobs);
			if(MALUtilities.isNotEmptyString(targetJob)){
				targetRoute = "spring-batch:" + targetJob;
			}else{
				throw new MalException("generic.error", 
					new String[] { "There are no processing jobs mapped for the Maintenance Invoices of Provider Number : " + parentProviderNumber});
			}
		}else{
			throw new MalException("generic.error", 
					new String[] { "Cannot process the incoming file.  The File/Resources name ("+ inputResourceUpper +") does not match a known pattern or proceessing is not turned on for Locations and Codes"});
		}

		return targetRoute;
	}
	
	// Note: this defaults to using the default set of a job has not been defined
	// if a "mismatched" set of jobs (less than 3 per supplier) has been defined then an error is thrown by the caller of this. 
	private String findJobNameByIndex(int targetJobIdx,List<String> targetJobs){
		String jobName = "";
		if(!MALUtilities.isEmpty(targetJobs)){
			try{
				jobName = targetJobs.get(targetJobIdx);
			}catch(IndexOutOfBoundsException ex){
				//TODO: maybe log a warning
			}
			
		}else{
			switch (targetJobIdx) {
            case LOCS_JOB_IDX:  jobName = IDEAL_STORE_LOC;
            	break;
            case CODES_JOB_IDX:  jobName = IDEAL_VENDOR_MAINT_CODE;
                break;
            case INVOICES_JOB_IDX:  jobName =  IDEAL_VENDOR_INVOICE;
                break;
            case MAPPING_JOB_IDX : jobName = IDEAL_DIRECT_MAP_CODES;
			}
		}
				
		return jobName;
	}

	public String getDeliveringDealerFolder() {
		return deliveringDealerFolder;
	}

	public void setDeliveringDealerFolder(String deliveringDealerFolder) {
		this.deliveringDealerFolder = deliveringDealerFolder;
	}
}
