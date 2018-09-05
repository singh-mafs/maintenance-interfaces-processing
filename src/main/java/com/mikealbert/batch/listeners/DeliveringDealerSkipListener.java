package com.mikealbert.batch.listeners;

import org.springframework.batch.core.SkipListener;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;

public class DeliveringDealerSkipListener implements SkipListener<Object, Object> {

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

    @Override
    public void onSkipInProcess(Object arg0, Throwable arg1) {
    	logger.info("Record was skipped while processing the file. " + arg1.getMessage());
    }

    @Override
    public void onSkipInRead(Throwable arg0) {
    	logger.info("Record: " + arg0.getMessage() + "was skipped while reading the file. ");
    }

    @Override
    public void onSkipInWrite(Object arg0, Throwable arg1) {
    	logger.info("Record was skipped while writting the file. ");
    }

}