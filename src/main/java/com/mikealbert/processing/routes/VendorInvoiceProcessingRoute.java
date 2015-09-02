package com.mikealbert.processing.routes;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.NoSuchEndpointException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.apache.camel.spi.DataFormat;

import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.processing.handlers.MalBusinessExceptionHandler;

public class VendorInvoiceProcessingRoute extends RouteBuilder {
	DataFormat exportFormat = new BeanIODataFormat(
	        "serviceProviderInvoiceMapping.xml",
	        "serviceProviderInvoice");
	
	DataFormat canonicalFormat = new BeanIODataFormat(
	        "vendorInvoiceMapping.xml",
	        "vendorInvoiceIdealFile");
	
	@Override
	public void configure() throws Exception {  
		from("jms:queue:queue.vendorInvoices?concurrentConsumers={{consumer.count}}")
		.setHeader("export").simple("{{export.fmt}}")
		.setExchangePattern(ExchangePattern.InOnly)
		.errorHandler(deadLetterChannel("jms:queue:dead.vendorInvoices?transferExchange=true").maximumRedeliveries(0))
		//TODO: there is an issue with a NoSuchEndpointException
		.onException(NoSuchEndpointException.class).handled(true).end()
		.to("direct:vendorInvoiceAdd");
		
		from("direct:vendorInvoiceAdd")
		.onException(MalBusinessException.class)
		.onWhen(header("reprocess").isNotEqualTo("true"))
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage()
		.process(new MalBusinessExceptionHandler())
		.to("jms:queue:errors.vendorInvoices")
		.end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:vendorInvoiceMappingProcess")	
		.wireTap("direct:checkNewVendorMaintCodes")
		.to("direct:saveInvoice");
		
		from("direct:saveInvoice")
		.choice()
            .when(header("export").isEqualTo("file"))
                .marshal(exportFormat)
                .setHeader(Exchange.FILE_NAME, simple("${header.parentName}_${date:now:yyyyMMdd}_J${header.loadId}.csv"))
                .to("file:{{files.export}}\\?fileExist=Append")
            .otherwise()
                .to("bean:vendorInvoiceSaveHandler")
				.filter(header("reprocess").isEqualTo("true"))
					.to("bean:errMsgRemoveHandler");
	
		//write a route to write this information to a file
		// and create a component to write the errors 
		from("jms:queue:errors.vendorInvoices?concurrentConsumers={{consumer.count}}")
		.to("bean:vendorInvoiceErrorEnrichProcess")
		.marshal(canonicalFormat)
		.setHeader(Exchange.FILE_NAME, simple("\\${header.inputFolderName}\\Errors\\Maintenance_Invoices_${date:now:yyyyMMdd}.err"))
		.to("file:{{files.input}}?fileExist=Append");
		
		//detect new codes
		//create VOs for those new code
		//put them in the "other" queue
		from("direct:checkNewVendorMaintCodes")
		.to("bean:detectNewVendorMaintCodes")
		.split(body())
		.to("jms:queue:queue.vendorCodes");
	}

}
