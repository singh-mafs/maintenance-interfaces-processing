package com.mikealbert.processing.routes;

import org.apache.camel.builder.RouteBuilder;

public class ErrorRemovalRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("cxf:bean:interfaceErrorsRemoval")
		.to("bean:errRemoveHeaderMappingProcess")
		.processRef("errMsgRemoveHandler");
	}

}
