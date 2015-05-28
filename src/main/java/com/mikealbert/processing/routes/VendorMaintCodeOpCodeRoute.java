package com.mikealbert.processing.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.NoSuchEndpointException;
import org.apache.camel.builder.RouteBuilder;

import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.processing.handlers.MalBusinessExceptionHandler;
import com.mikealbert.processing.processors.VendorMaintCodeOpCodeRoutingProcess;

public class VendorMaintCodeOpCodeRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {  
		from("jms:queue:queue.vendorCodes?concurrentConsumers={{consumer.count}}")
		.setExchangePattern(ExchangePattern.InOnly)
		.errorHandler(deadLetterChannel("jms:queue:dead.vendorCodes?transferExchange=true").maximumRedeliveries(0))
		//TODO: there is an issue with a NoSuchEndpointException
		.onException(NoSuchEndpointException.class).handled(true).end()
		.dynamicRouter(method(VendorMaintCodeOpCodeRoutingProcess.class,"determineRoute"));
		
		from("direct:vendorMaintCodeAdd")
		.onException(MalBusinessException.class).onWhen(header("reprocess").isNotEqualTo("true"))
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage().process(new MalBusinessExceptionHandler()).to("jms:queue:errors.vendorCodes").end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:vendorMaintCodeAddMappingProcess")
		.to("bean:vendorMaintCodeSaveHandler")
		.filter(header("reprocess").isEqualTo("true"))
			.to("bean:errMsgRemoveHandler");
		
		from("direct:vendorMaintCodeUpdate")
		.onException(MalBusinessException.class).onWhen(header("reprocess").isNotEqualTo("true"))
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage().process(new MalBusinessExceptionHandler()).to("jms:queue:errors.vendorCodes").end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:vendorMaintCodeUpdateMappingProcess")
		.to("bean:vendorMaintCodeSaveHandler")
		.filter(header("reprocess").isEqualTo("true"))
			.to("bean:errMsgRemoveHandler");
		
		from("direct:vendorMaintCodeDelete")
		.onException(MalBusinessException.class).onWhen(header("reprocess").isNotEqualTo("true"))
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage().process(new MalBusinessExceptionHandler()).to("jms:queue:errors.vendorCodes").end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:vendorMaintCodeDeleteMappingProcess")
		.to("bean:vendorMaintCodeSaveHandler")
		.filter(header("reprocess").isEqualTo("true"))
			.to("bean:errMsgRemoveHandler");
		
		
	}

}
