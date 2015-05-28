package com.mikealbert.processing.processors;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.DynamicRouter;
import org.apache.camel.Properties;

import com.mikealbert.data.vo.StoreLocationVO;

public class OpCodeRoutingProcess {
	
	private static final String VENDOR_ADD = "direct:vendorLocationsAdd";
	private static final String VENDOR_UPDATE = "direct:vendorLocationsUpdate";
	private static final String VENDOR_DELETE = "direct:vendorLocationsDelete";
	
	@DynamicRouter(ignoreInvalidEndpoints=true) 
	public String determineRoute(@Body Object body, @Properties Map<String, Object> properties){
		// if we have already routed this request (storeLocationVO) skip it
		if(getInvokedMarker(properties)){
			return null;
		}else if(body instanceof StoreLocationVO && ((StoreLocationVO) body).getOperationCode().equalsIgnoreCase("A")){ 
			setInvokedMarker(properties,true);
			return VENDOR_ADD;
		}else if(body instanceof StoreLocationVO && ((StoreLocationVO) body).getOperationCode().equalsIgnoreCase("M")){ 
			setInvokedMarker(properties,true);
			return VENDOR_UPDATE;
		}else if(body instanceof StoreLocationVO && ((StoreLocationVO) body).getOperationCode().equalsIgnoreCase("D")){ 
			setInvokedMarker(properties,true);
			return VENDOR_DELETE;
		}else{
			return null;
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
