package com.mikealbert.batch.listeners;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

import com.mikealbert.service.util.VelocityTemplateHelper;
import com.mikealbert.service.util.email.Email;
import com.mikealbert.service.util.email.EmailService;

/**
 * 
 * @author Duncan
 * 
 * Inspired by @see http://ice09.wordpress.com/2010/08/15/spring-batch-with-gmail-notification-using-velocity/
 * 
 */

public class StatusMailJobListener implements JobExecutionListener {
    private EmailService mailSendService;
    private VelocityTemplateHelper mailBodyHelper;
    private Email mailMessage;
 
    @Override
    public void afterJob(JobExecution jobExecution) {
        String exitCode = jobExecution.getExitStatus().getExitCode();
        boolean withoutErrors = false;
        if (exitCode.equals(ExitStatus.COMPLETED.getExitCode())) {
        	withoutErrors = true;
        }
        sendMail(jobExecution, withoutErrors);
       
    }
    private void sendMail(final JobExecution jobExecution, boolean withoutErrors) {
    	String inputFile;
    	String executionDateTime = null;
    	int totalRecords = 0;
    	int readWriteErrors = 0;
    	int validationErrors = 0;
    	int writeSuccess = 0;
    	
    	
    	JobParameters params = jobExecution.getJobInstance().getJobParameters();
    	inputFile = params.getString("inputResource");
    	executionDateTime = jobExecution.getStartTime().toString();
    	
    	Collection<StepExecution> jobStepResults = jobExecution.getStepExecutions();
    	for(StepExecution step : jobStepResults){
    		if(step.getStepName().toUpperCase().startsWith("LOAD")){
        		readWriteErrors = step.getReadSkipCount() + step.getWriteSkipCount();
        		writeSuccess = step.getWriteCount();
        		validationErrors = step.getProcessSkipCount();
        		totalRecords = step.getReadCount() + step.getReadSkipCount();
        		break;
    		}
    	}

    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("executionDateTime", executionDateTime);
    	data.put("inputFile", inputFile);
    	data.put("totalRecords", totalRecords);
    	data.put("readWriteErrors", readWriteErrors);
    	data.put("validationErrors", validationErrors);
    	data.put("writeSuccess", writeSuccess);

    	mailMessage.setMessage(mailBodyHelper.processTemplate(data));
    	mailSendService.sendEmail(mailMessage);
    }
 
     public void beforeJob(JobExecution jobExecution) {
        Map<String, String> values = new HashMap<String, String>();

        jobExecution.getExecutionContext().put("values", values);
    }
    
	public EmailService getMailSendService() {
		return mailSendService;
	}
	public void setMailSendService(EmailService mailSendService) {
		this.mailSendService = mailSendService;
	}
	public VelocityTemplateHelper getMailBodyHelper() {
		return mailBodyHelper;
	}
	public void setMailBodyHelper(VelocityTemplateHelper mailBodyHelper) {
		this.mailBodyHelper = mailBodyHelper;
	}
	public Email getMailMessage() {
		return mailMessage;
	}
	public void setMailMessage(Email mailMessage) {
		this.mailMessage = mailMessage;
	}

}