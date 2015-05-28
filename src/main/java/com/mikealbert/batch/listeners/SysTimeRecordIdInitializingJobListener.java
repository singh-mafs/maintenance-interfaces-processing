package com.mikealbert.batch.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class SysTimeRecordIdInitializingJobListener implements JobExecutionListener {

    public void afterJob(JobExecution jobExecution) {
    	
    }

    public void beforeJob(JobExecution jobExecution) {
        long x =  System.currentTimeMillis();
        // trim the leading 1 digits off of it.
        Long startingRecordId = new Long(String.valueOf(x).substring(1));
        jobExecution.getExecutionContext().put("recordId", startingRecordId.longValue());
    }
}