package com.mikealbert.processing.routes;

import org.apache.camel.NoSuchEndpointException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.batch.item.validator.ValidationException;

import com.mikealbert.processing.processors.VendorMaintCodeOpCodeRoutingProcess;

public class CodeReprocessingRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("cxf:bean:vendorCodeReprocessing")
		.to("bean:supIdHeaderMappingProcess")
		//.onException(ValidationException.class).handled(true).end()
		.to("bean:vendorMaintCodeValidator")
		//TODO: there is an issue with a NoSuchEndpointException
		//.onException(NoSuchEndpointException.class).handled(true).end()
		.dynamicRouter(method(VendorMaintCodeOpCodeRoutingProcess.class,"determineRoute"));
	}

}
