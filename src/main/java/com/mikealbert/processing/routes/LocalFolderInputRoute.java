package com.mikealbert.processing.routes;

import org.apache.camel.builder.RouteBuilder;

import com.mikealbert.processing.processors.ConfigurableFileRoutingProcess;

public class LocalFolderInputRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		errorHandler(loggingErrorHandler("com.mikealbert.processing"));

		from("file:{{files.input}}?recursive=true&flatten=false&minDepth=2&maxDepth=2&preMove=../.InProgress")
		.processRef("fileNameHandler")
		.to("file:{{local.staged.dir}}")
		.to("direct:stagedFiles");
		
		from("direct:stagedFiles")
		.to("bean:jobParamHandler")
		.dynamicRouter(method(ConfigurableFileRoutingProcess.class,"determineRoute"));
		
	}


	
}
