package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.mikealbert.data.vo.InterfaceErrorsVO;
import com.mikealbert.service.util.jms.JmsHelperService;

@Component("errMsgRemoveHandler")
public class ErrMsgRemoveHandler implements Processor {
	
	@Resource JmsHelperService jmsHelperService;

	private void removeInterfaceError(String interfaceType, String messageId){
		jmsHelperService.deleteMessageById(InterfaceErrorsVO.INTERFACE_TYPES.get(interfaceType), messageId);
	}

	@Override
	public void process(Exchange ex) throws Exception {
		String messageId = (String) ex.getIn().getHeader("origMsgId");
		String interfaceType = (String) ex.getIn().getHeader("interfaceType");
		removeInterfaceError(interfaceType,messageId);
		ex.getOut().setBody(null);
	}

}




