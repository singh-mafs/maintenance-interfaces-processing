package com.mikealbert.processing.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.vo.InterfaceErrorsVO;
import com.mikealbert.exception.MalException;
import com.mikealbert.service.ServiceProviderService;
import com.mikealbert.service.util.jms.JmsHelperService;
import com.mikealbert.util.MALUtilities;
import com.mikealbert.util.ObjectUtils;

@Component("errorsBrowseHandler")
public class InterfaceErrorsBrowseHandler  implements Processor {
	
	@Resource JmsHelperService jmsHelperService;
	@Resource ServiceProviderService providerService;

	
	@Override
	public void process(Exchange ex) throws Exception {
		String interfaceType = ex.getIn().getBody(String.class);
		ArrayList<InterfaceErrorsVO> errors = getInterfaceErrors(interfaceType);
		List<Object> resultList = new ArrayList<Object>(); 
		resultList.add(errors); 
		ex.getOut().setBody(resultList);
	}
	
	private ArrayList<InterfaceErrorsVO> getInterfaceErrors(String interfaceType){
		List<Message> messages = jmsHelperService.getMessagesInQueue(InterfaceErrorsVO.INTERFACE_TYPES.get(interfaceType));
		ArrayList<InterfaceErrorsVO> errorVOs = new ArrayList<InterfaceErrorsVO>();
		InterfaceErrorsVO errorVO;
		List<String> errMsgs;
		List<String> errFields;
		
		try {
			if((!MALUtilities.isEmpty(messages)) && messages.size() > 0){
				for(Message msg : messages){
					ServiceProvider provider = providerService.getServiceProvider(msg.getLongProperty("parentProviderId"));
					
	    			ObjectMessage objMsg = (ObjectMessage)msg;
	    			Object error = objMsg.getObject();
	
	    			errorVO = new InterfaceErrorsVO();
	    			
	    			//set error messages and error fields as separate arrays
	    			
	    			errorVO.setMessageId(msg.getJMSMessageID());
	    			// error fields
	    			errFields = new ArrayList<String>();
	    			// error messages
	    			errMsgs = new ArrayList<String>();  
	    			
	    			if(!MALUtilities.isEmpty(msg.getObjectProperty("fieldErrors"))){
	    				HashMap<String, ArrayList<String>> fieldErrors = (HashMap<String, ArrayList<String>>) msg.getObjectProperty("fieldErrors");
		    			
	    				errFields.addAll(fieldErrors.keySet());
			        	Collection<ArrayList<String>> fieldErrorsVals =  fieldErrors.values();
			        	
			        	for(List<String> fieldErrMsgs : fieldErrorsVals){
			        		for(String errMsg: fieldErrMsgs){
			        			errMsgs.add(errMsg);
			        		}
			        	}
	    			}
	    			
	    			String recordError = msg.getStringProperty("recordError");
	    			
	    			if(MALUtilities.isNotEmptyString(recordError)){
	    				errMsgs.add(recordError);
	    			}
	    			
	    			String[] errFieldArr = errFields.toArray(new String[errFields.size()]);
	    			String[] errArr = errMsgs.toArray(new String[errMsgs.size()]);
		        	
	    			errorVO.setErrorMessages(errArr);
	    			errorVO.setErrorFields(errFieldArr);
	    			
	    			errorVO.setParentProviderId(msg.getLongProperty("parentProviderId"));
	    			errorVO.setSourceObject(error);
	        		ObjectUtils.setProperty(error, "messageId", msg.getJMSMessageID());
	        		String providerDesc = provider.getServiceProviderName()  + " No: " + provider.getServiceProviderNumber(); 
	        		ObjectUtils.setProperty(error, "parentProvider", providerDesc);
	        		
	        		errorVOs.add(errorVO);
	    			
	    			
				}		
			}
		} catch (JMSException e) {
			throw new MalException("generic.error.occured.while", 
					new String[] { "browsing the interface type list for : " + interfaceType}, e);
		}
			
		return errorVOs;
		
	}
}




