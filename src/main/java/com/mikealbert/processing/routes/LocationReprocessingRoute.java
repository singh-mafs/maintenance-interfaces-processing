package com.mikealbert.processing.routes;

import org.apache.camel.NoSuchEndpointException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.batch.item.validator.ValidationException;

import com.mikealbert.batch.exceptions.MalFieldValidationException;
import com.mikealbert.processing.processors.OpCodeRoutingProcess;

public class LocationReprocessingRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("cxf:bean:vendorLocationReprocessing")
		//.errorHandler(defaultErrorHandler())
		//.onException(NoSuchEndpointException.class).handled(true).end()
		.to("bean:supIdHeaderMappingProcess")
		.to("bean:storeLocationValidator")
		//TODO: there is an issue with a NoSuchEndpointException
		//.onException(NoSuchEndpointException.class).handled(true).end()
		.dynamicRouter(method(OpCodeRoutingProcess.class,"determineRoute"));
	}

}
