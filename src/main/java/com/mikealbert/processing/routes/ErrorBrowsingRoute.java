package com.mikealbert.processing.routes;

import org.apache.camel.builder.RouteBuilder;

public class ErrorBrowsingRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("cxf:bean:interfaceErrorsBrowsing")
		.processRef("errorsBrowseHandler");
	}

}
