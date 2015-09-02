package com.mikealbert.batch.listeners;

import java.util.HashMap;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import com.mikealbert.batch.exceptions.MalFieldValidationException;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;

public class ValdiationExceptionListener {

	private JmsTemplate errorWriterTemplate;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	private Long parentProviderId;
	private String inputResource;
	private HashMap<String,List<String>> fieldErrors;
	
	@Value("#{jobParameters[parentProviderId]}")
	public void setParentProviderId(Long parentProviderId) {
		this.parentProviderId = parentProviderId;
	}
	
	@Value("#{jobParameters[inputResource]}")
	public void setInputResource(String inputResource) {
		this.inputResource = inputResource;
	}
	
	@OnSkipInProcess
	public void onValidationError(Object item, Throwable t){
		// if it's "our" validation exception
		if(t instanceof MalFieldValidationException){
			// we need to publish these messages to a Error queue
			// we need to include the Job parameter for the supplier
			// we need to somehow get the list of fields and messages out of the validator (i.e. subclass Validation Exception)
			// so I can include an extra property that will hold the fields in error.
			fieldErrors = ((MalFieldValidationException) t).getFieldExceptions();
			
			errorWriterTemplate.convertAndSend(item,new MessagePostProcessor() {
					@Override
					public Message postProcessMessage(Message message) throws JMSException {
						String[] inFileParts	 = inputResource.split("\\\\");
					    String   inputFolderName = inFileParts[inFileParts.length -4];

						message.setLongProperty("parentProviderId", parentProviderId);
						message.setObjectProperty("fieldErrors", fieldErrors);
						message.setStringProperty("inputFolderName", inputFolderName);
						return message;
					}
				});
			
			
			logger.info("A Validation Error Occured while trying to process line - " + item.toString());
			
		}else{
			logger.error(t,"A Error Occured while trying to validate/process line - " + item.toString() + " from the following location : " + inputResource + " --- ");
		}
		
	}
	
	@OnReadError
	public void onReadError(Exception ex){
		logger.error(ex,"An Error occured while reading a record from the following location : " + inputResource + " --- ");		
	}
	
	@OnWriteError
	public void onWriteError(Exception ex, List items){
		logger.error(ex,"An Error occured while writing a record from the following location : " + inputResource + " --- ");
	}

	public JmsTemplate getErrorWriter() {
		return errorWriterTemplate;
	}

	public void setErrorWriter(JmsTemplate errorWriterTemplate) {
		this.errorWriterTemplate = errorWriterTemplate;
	}
}
