package com.mikealbert.batch.processors;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemProcessor;

import com.mikealbert.batch.listeners.StepExecutionListenerCtxInjector;
import com.mikealbert.data.vo.FuelCardVO;
import com.mikealbert.util.ObjectUtils;

public class LoadIdProcessor<T> implements ItemProcessor<T, T> {
	
	@Resource StepExecutionListenerCtxInjector ctxInjector;
	
	@Override
	public T process(T targetItem) throws Exception {
		Long loadId = ctxInjector.getJobExecutionCtx().getLong("loadId");
		//TODO: We will be making it generic to get load id and set in object for any kind of load process
		//As of now com data is getting its load id base don a sequence in database but other loads are getting
		//load id generated and maintained in MVI itself. Com data is being treated differently here.
		if(targetItem instanceof FuelCardVO){
			Long fuelUploadloadId = ctxInjector.getJobExecutionCtx().getLong("fuelUploadloadId");
			ObjectUtils.setProperty(targetItem,"loadId",fuelUploadloadId);
		}else{
			ObjectUtils.setProperty(targetItem,"loadId",loadId);
		}
		
		return targetItem;
	}
}
