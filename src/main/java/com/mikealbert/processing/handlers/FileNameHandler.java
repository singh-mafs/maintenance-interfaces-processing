package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.service.ServiceProviderService;

@Component("fileNameHandler")
public class FileNameHandler implements Processor {
	
	/**
	 * This handler is the "ideal" file name handler it uses the file name to determine the parent 
	 * supplier record (based upon our naming convention) and handles file re-naming based upon this information
	 * we will need to work out a (configuration not convention) based FileName handler longer term to handle
	 * filename that do not match what we need them to so we can rename them to what we do need for staging.
	 */
	
	@Resource ServiceProviderService serviceProviderService;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange ex) throws Exception {
		Message in =  ex.getIn();
		Message out =  ex.getOut();

		Object bdy = in.getBody();
		
		if(bdy != null){
			String[] inFileParts;
			String parentPart;
			String fileNamePart;
			String outFileName;
			
			String relativePath = in.getHeader("camelfilerelativepath").toString();
			inFileParts = relativePath.split("\\\\");
			parentPart = inFileParts[inFileParts.length -4];
			fileNamePart = inFileParts[inFileParts.length -1];
			ServiceProvider parent = serviceProviderService.getParentProviderByNameInFileFmt(parentPart+"%");
			String fullPath = in.getHeader("camelfilepath").toString();
			outFileName = "SUP_" + parent.getServiceProviderId() + "-" + fileNamePart;
			out.setHeaders(in.getHeaders());
			out.setBody(bdy);
			out.setMessageId(in.getMessageId());
			
	        // set the output filename using java code logic, notice that this is done by setting
	        // a special header property of the out exchange
			out.setHeader("CamelFileName", outFileName);
			
			// TODO: should I create a separate handler? pass the service provider number in the header as well
			out.setHeader("parentProviderNumber", parent.getServiceProviderNumber());
			
			logger.info("Staging file - " + fullPath + " : renamed as " + outFileName);				
		}
	}
}




