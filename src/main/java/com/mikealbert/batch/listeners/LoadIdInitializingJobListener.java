package com.mikealbert.batch.listeners;

import javax.annotation.Resource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import com.mikealbert.service.FuelUploadService;

public class LoadIdInitializingJobListener implements JobExecutionListener {
	@Resource
	private	FuelUploadService	fuelUploadService;
    public void afterJob(JobExecution jobExecution) {
    	
    }

    public void beforeJob(JobExecution jobExecution) {
        //TODO: use the sequence from the DB!!
    	jobExecution.getExecutionContext().put("loadId",jobExecution.getJobInstance().getId());
    	Long fuelUploadloadId	=	fuelUploadService.getNextFuelUploadLoadId();
    	jobExecution.getExecutionContext().put("fuelUploadloadId",fuelUploadloadId);
    }
}