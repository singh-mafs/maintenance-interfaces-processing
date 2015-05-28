package com.mikealbert.batch.item.jms;

import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.util.Assert;

import com.mikealbert.batch.listeners.StepExecutionListenerCtxInjector;

public class JmsItemAndJobParameterWriter<T> implements ItemWriter<T> {
	
	protected Log logger = LogFactory.getLog(getClass());

	private JmsOperations jmsTemplate;

	private Long parentProviderId;
	
	@Resource StepExecutionListenerCtxInjector ctxInjector;

	@Value("#{jobParameters[parentProviderId]}")
	public void setParentProviderId(Long parentProviderId) {
		this.parentProviderId = parentProviderId;
	}

	/**
	 * Setter for JMS template.
	 * 
	 * @param jmsTemplate
	 *            a {@link JmsOperations} instance
	 */
	public void setJmsTemplate(JmsOperations jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		if (jmsTemplate instanceof JmsTemplate) {
			JmsTemplate template = (JmsTemplate) jmsTemplate;
			Assert
					.isTrue(template.getDefaultDestination() != null
							|| template.getDefaultDestinationName() != null,
							"JmsTemplate must have a defaultDestination or defaultDestinationName!");
		}
	}
	
	/**
	 * Send the items one-by-one to the default destination of the jms template.
	 * 
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends T> items) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Writing to JMS with " + items.size() + " items.");
		}

		for (T item : items) {
			jmsTemplate.convertAndSend(item,new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message)
						throws JMSException {
					message.setLongProperty("parentProviderId", parentProviderId);
					
					long loadId = ctxInjector.getJobExecutionCtx().getLong("loadId");
					if(loadId != 0){
						message.setStringProperty("JMSXGroupID", Long.toString(loadId));
					}
					
					return message;
				}
			});
		}

	}
}
