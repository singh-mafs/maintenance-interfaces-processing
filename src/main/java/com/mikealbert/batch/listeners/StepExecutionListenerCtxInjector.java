package com.mikealbert.batch.listeners;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
 
 
public class StepExecutionListenerCtxInjector
{
    private ExecutionContext stepExecutionCtx;
     
    private ExecutionContext jobExecutionCtx;
     
    @BeforeStep
    public void beforeStep(StepExecution stepExecution)
    {
        stepExecutionCtx = stepExecution.getExecutionContext();
        jobExecutionCtx = stepExecution.getJobExecution().getExecutionContext();
    }
 
 
    public ExecutionContext getStepExecutionCtx()
    {
        return stepExecutionCtx;
    }
 
    public ExecutionContext getJobExecutionCtx()
    {
        return jobExecutionCtx;
    }
}