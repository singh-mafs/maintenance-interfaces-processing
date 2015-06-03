package com.mikealbert.batch.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class LoadIdInitializingJobListener implements JobExecutionListener {
    public void afterJob(JobExecution jobExecution) {
    	
    }

    public void beforeJob(JobExecution jobExecution) {
        //TODO: use the sequence from the DB!!
    	jobExecution.getExecutionContext().put("loadId",jobExecution.getJobInstance().getId());
    }
}