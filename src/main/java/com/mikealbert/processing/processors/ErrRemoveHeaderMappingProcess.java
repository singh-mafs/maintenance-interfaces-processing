package com.mikealbert.processing.processors;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.mikealbert.util.MALUtilities;

@Component("errRemoveHeaderMappingProcess")
public class ErrRemoveHeaderMappingProcess implements Processor {
	@Override
	public void process(Exchange ex) throws Exception {
		List<String> inputs = ex.getIn().getBody(List.class);
		if(!MALUtilities.isEmpty(inputs)){
			String interfaceType = inputs.get(0);
			String messageId = inputs.get(1);
			ex.getOut().setHeader("origMsgId", messageId);
			ex.getOut().setHeader("reprocess", true);
			ex.getOut().setHeader("interfaceType", interfaceType);
		}	
	}

}
