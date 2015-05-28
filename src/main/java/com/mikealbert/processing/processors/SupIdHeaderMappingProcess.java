package com.mikealbert.processing.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;
import org.springframework.stereotype.Component;

import com.mikealbert.util.ObjectUtils;

@Component("supIdHeaderMappingProcess")
public class SupIdHeaderMappingProcess implements Processor {
	@Override
	public void process(Exchange ex) throws Exception {
		Message in =  ex.getIn();
		MessageContentsList bdy = (MessageContentsList) in.getBody();
		if(bdy != null){
			Long parentProviderId = (Long) bdy.get(1);
			ex.getOut().setHeader("parentProviderId",parentProviderId);	
			ex.getOut().setHeader("reprocess", true);
			ex.getOut().setHeader("origMsgId", ObjectUtils.getProperty(bdy.get(0), "messageId"));
			ex.getOut().setHeader("interfaceType", (String) bdy.get(2));
			ex.getOut().setBody(bdy.get(0));
		}
		
	}

}
