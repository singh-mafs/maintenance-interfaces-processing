package com.mikealbert.processing.handlers;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.dao.MakeDAO;
import com.mikealbert.data.entity.Make;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.service.ServiceProviderService;

@Component("fileNameHandler")
public class FileNameHandler implements Processor {
    
  @Resource ServiceProviderService serviceProviderService;
  @Resource MakeDAO makeDAO;	
  
  private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

  @Override
  public void process(Exchange ex) throws Exception {
    Message in  = ex.getIn();
    Message out = ex.getOut();
    Object  bdy = in.getBody();

    Make make = null;
    ServiceProvider 	parent = null;
    String      		outFileName;
    if(bdy != null) {
      String      		fullPath      		= in.getHeader("camelfilepath").toString();
      String      		relativePath    	= in.getHeader("camelfilerelativepath").toString();
      String[]    		inFileParts     	= relativePath.split("\\\\");
      String      		parentPart      	= inFileParts[inFileParts.length -4];
      String      		fileNamePart    	= inFileParts[inFileParts.length -1];
      Long      		serviceProviderId   = new Long(parentPart.split("_")[0].toString());
      
      if(parentPart.toLowerCase().contains("delivering")){
    	  make = makeDAO.findById(Long.valueOf(serviceProviderId)).orElse(null);
    	  outFileName     	= "MAK_" + make.getMakId() + "-" + fileNamePart;  
    	  out.setHeaders(in.getHeaders());
          out.setBody(bdy);
          out.setMessageId(in.getMessageId());
    	  out.setHeader("parentProviderNumber", make.getMakId());
    	  out.setHeader("make", make.getMakeDesc());
    	  out.setHeader("folderName", parentPart);
      }else{
    	  parent        		= serviceProviderService.getServiceProvider(serviceProviderId);
          outFileName     		= "SUP_" + parent.getServiceProviderId() + "-" + fileNamePart;  
          out.setHeaders(in.getHeaders());
          out.setBody(bdy);
          out.setMessageId(in.getMessageId());
          out.setHeader("parentProviderNumber", parent.getServiceProviderNumber());
          
      }
      
      // set the output filename using java code logic, notice that this is done by setting
      // a special header property of the out exchange
      out.setHeader("CamelFileName", outFileName);

      // TODO: should I create a separate handler? pass the service provider number in the header as well
      

      logger.info("Staging file - " + fullPath + " : renamed as " + outFileName);
    }
  }
}
