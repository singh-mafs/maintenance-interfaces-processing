package com.mikealbert.processing.handlers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.mikealbert.exception.MalBusinessException;

public class MalBusinessExceptionHandler implements Processor  {

	@Override
	public void process(Exchange ex) throws Exception {
		MalBusinessException exception = ex.getProperty(Exchange.EXCEPTION_CAUGHT, MalBusinessException.class);
		ex.getIn().setHeader("recordError", exception.getMessage());
		Object bdy = ex.getIn().getBody();
		ex.getIn().setBody(bdy);
	}

}
