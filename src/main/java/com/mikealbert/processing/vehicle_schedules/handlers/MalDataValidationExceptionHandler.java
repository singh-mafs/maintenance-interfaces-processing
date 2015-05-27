package com.mikealbert.processing.vehicle_schedules.handlers;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.mikealbert.exception.MalDataValidationException;

public class MalDataValidationExceptionHandler implements Processor  {

	@Override
	public void process(Exchange ex) throws Exception {
		MalDataValidationException exception = ex.getProperty(Exchange.EXCEPTION_CAUGHT, MalDataValidationException.class);
		ex.getIn().setHeader("recordError", exception.getMessage());
		Object bdy = ex.getIn().getBody();
		ex.getIn().setBody(bdy);
	}

}
