package com.mikealbert.batch.jobs;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;

@ContextConfiguration(locations={"classpath:applicationContextTest.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class)
public class StoreLocationJobTest {
    @Resource JobLauncher launcher;
    @Resource JobRepository repo;
    @Resource @Qualifier("storeLocationJob") Job storeLocationJob;
    
    @Test 
    public void testStoreLocationJobSuccessAdd() throws Exception {
    	JobExecution jobExecution = getTestUtils().launchJob(
    			new JobParametersBuilder()
	            .addDate("date", new Date())	
	            .addString("inputResource", "classpath:Success-Store_Locations_MMDDYYYY.csv")
	            .addLong("parentProviderId", 19396L)
	            .addLong("run.id", 1L)
	            .toJobParameters());
    	// the job completed successfully
    	assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode()); 
    	// we only skipped the header
    	StepExecution[] stepExec = new StepExecution[1];
    	stepExec = jobExecution.getStepExecutions().toArray(stepExec);
    	assertEquals(stepExec[0].getSkipCount(),1);
    }
    
    @Test 
    public void testStoreLocationJobSuccessModify() throws Exception {
    	JobExecution jobExecution = getTestUtils().launchJob(
    			new JobParametersBuilder()
	            .addDate("date", new Date())	
	            .addString("inputResource", "classpath:Modify-Store_Locations_MMDDYYYY.csv")
	            .addLong("parentProviderId", 19396L)
	            .addLong("run.id", 1L)
	            .toJobParameters());
    	// the job completed successfully
    	assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode()); 
    	// we only skipped the header
    	StepExecution[] stepExec = new StepExecution[1];
    	stepExec = jobExecution.getStepExecutions().toArray(stepExec);
    	assertEquals(stepExec[0].getSkipCount(),1);
    }
    
    @Test 
    public void testStoreLocationJobSuccessDelete() throws Exception {
    	JobExecution jobExecution = getTestUtils().launchJob(
    			new JobParametersBuilder()
	            .addDate("date", new Date())	
	            .addString("inputResource", "classpath:Delete-Store_Locations_MMDDYYYY.csv")
	            .addLong("parentProviderId", 19396L)
	            .addLong("run.id", 1L)
	            .toJobParameters());
    	// the job completed successfully
    	assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode()); 
    	// we only skipped the header
    	StepExecution[] stepExec = new StepExecution[1];
    	stepExec = jobExecution.getStepExecutions().toArray(stepExec);
    	assertEquals(stepExec[0].getSkipCount(),1);
    }
    
    private JobLauncherTestUtils getTestUtils(){
    	JobLauncherTestUtils testUtils = new JobLauncherTestUtils();
    	testUtils.setJobRepository(repo);
    	testUtils.setJobLauncher(launcher);
    	testUtils.setJob(storeLocationJob);
    	return testUtils;
    }
    
    
}
