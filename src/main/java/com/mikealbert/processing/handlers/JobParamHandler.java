package com.mikealbert.processing.handlers;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;

@Component("jobParamHandler")
public class JobParamHandler {

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	/**
	 * This handler extracts the parent supplier id and processing path out of the exchange and 
	 * sets it as a header so it will be converted into a Job Parameter and passed to the job.
	 * @param ex
	 */
	@Handler
	public void setupJobHeaders(Exchange ex){
		Message in =  ex.getIn();
		Object bdy = in.getBody();
		if(bdy != null){
			String camelFileName = in.getHeader("CamelFileName").toString();
			String parentProviderNumber = in.getHeader("parentProviderNumber").toString();
			String parentPrefix = camelFileName.split("-")[0];
			String parentProviderStr = parentPrefix.split("_")[1];
			String stagedResouceName = "file:" + in.getHeader("CamelFileAbsolutePath").toString();
			ex.getOut().setHeader("inputResource", stagedResouceName);
			ex.getOut().setHeader("parentProviderId", Long.parseLong(parentProviderStr));
			ex.getOut().setHeader("parentProviderNumber", parentProviderNumber);
			ex.getOut().setHeader("make", in.getHeader("make") != null ? in.getHeader("make") : null);
			ex.getOut().setHeader("folderName", in.getHeader("folderName") != null ? in.getHeader("folderName") : null);
			logger.info("Calling Bath Job for : " + stagedResouceName);				
		}
	}
}