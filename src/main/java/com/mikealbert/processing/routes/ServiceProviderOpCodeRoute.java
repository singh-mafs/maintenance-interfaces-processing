package com.mikealbert.processing.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.NoSuchEndpointException;
import org.apache.camel.builder.RouteBuilder;

import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.processing.handlers.MalBusinessExceptionHandler;
import com.mikealbert.processing.processors.OpCodeRoutingProcess;

public class ServiceProviderOpCodeRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("jms:queue:queue.vendorLocations?concurrentConsumers={{consumer.count}}")
		.setExchangePattern(ExchangePattern.InOnly)
		.errorHandler(deadLetterChannel("jms:queue:dead.vendorLocations?transferExchange=true").maximumRedeliveries(0))
		//TODO: there is an issue with a NoSuchEndpointException
		.onException(NoSuchEndpointException.class).handled(true).end()
		.dynamicRouter(method(OpCodeRoutingProcess.class,"determineRoute"));
		
		from("direct:vendorLocationsAdd")
		.onException(MalBusinessException.class).onWhen(header("reprocess").isNotEqualTo("true"))
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage().process(new MalBusinessExceptionHandler()).to("jms:queue:errors.vendorLocations").end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:storeAddMappingProcess")
		.to("bean:serviceProviderSaveHandler")
		.filter(header("reprocess").isEqualTo("true"))
			.processRef("errMsgRemoveHandler");
		
		from("direct:vendorLocationsUpdate")
		.onException(MalBusinessException.class).onWhen(header("reprocess").isNotEqualTo("true"))
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage().process(new MalBusinessExceptionHandler()).to("jms:queue:errors.vendorLocations").end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:storeUpdateMappingProcess")
		.to("bean:serviceProviderSaveHandler")
		.filter(header("reprocess").isEqualTo("true"))
			.processRef("errMsgRemoveHandler");
		
		from("direct:vendorLocationsDelete")
		.onException(MalBusinessException.class).onWhen(header("reprocess").isNotEqualTo("true"))
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage().process(new MalBusinessExceptionHandler()).to("jms:queue:errors.vendorLocations").end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:storeDeleteMappingProcess")
		.to("bean:serviceProviderSaveHandler")
		.filter(header("reprocess").isEqualTo("true"))
			.processRef("errMsgRemoveHandler");
	}

}
