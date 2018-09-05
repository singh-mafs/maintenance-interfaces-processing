package com.mikealbert.batch.jobs;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@ContextConfiguration(locations={"classpath:applicationContextTest.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class)
public class DeliveringDealerJobTest {
	    @Resource JobLauncher launcher;
	    @Resource JobRepository repo;
	    @Resource @Qualifier("deliveringDealerJob") Job deliveringDealerJob;
	    
	    @Test 
	    public void testStoreLocationJobSuccessAdd() throws Exception {
	    	JobExecution jobExecution = getTestUtils().launchJob(
	    			new JobParametersBuilder()
		            .addString("inputResource", "classpath:ToyotaExecutiveDeliveryListMay2018.csv")
		            .toJobParameters());
	    	
	    	// the job completed successfully
	    	assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode()); 
	    	// we only skipped the header
	    	StepExecution[] stepExec = new StepExecution[1];
	    	stepExec = jobExecution.getStepExecutions().toArray(stepExec);
	    	assertEquals(stepExec[0].getSkipCount(),0);
	    }
	    
	    private JobLauncherTestUtils getTestUtils(){
	    	JobLauncherTestUtils testUtils = new JobLauncherTestUtils();
	    	testUtils.setJobRepository(repo);
	    	testUtils.setJobLauncher(launcher);
	    	testUtils.setJob(deliveringDealerJob);
	    	return testUtils;
	    }
	
}
