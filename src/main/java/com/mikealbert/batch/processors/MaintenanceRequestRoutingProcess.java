package com.mikealbert.batch.processors;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.DynamicRouter;
import org.apache.camel.Properties;

import com.mikealbert.data.entity.MaintenanceRequest;
import com.mikealbert.service.ServiceProviderService;

public class MaintenanceRequestRoutingProcess {
	
	private static final String AUTOCOMPLETE_ROUTE = "direct:maintenanceRequestAutoCompleteFlow";
	private static final String STANDARD_ROUTE = "direct:maintenanceRequestStandardFlow";
	
	@Resource ServiceProviderService serviceProviderService;
	
	@DynamicRouter
	public String determineRoute(MaintenanceRequest mrq, @Properties Map<String, Object> properties){
		// if we have already routed this request (mrq) skip it
		if(getInvokedMarker(properties)){
			return null;
		// if it is an auto complete provider route it to the auto-complete route
		}else if(serviceProviderService.isAutoCompleteServiceProvider(mrq.getServiceProvider())){ 
			setInvokedMarker(properties,true);
			return AUTOCOMPLETE_ROUTE;
		}else{ // else route it to the standard route
			setInvokedMarker(properties,true);
			return STANDARD_ROUTE;
		}
		
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

}
