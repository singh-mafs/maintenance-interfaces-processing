package com.mikealbert.batch.processors;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemProcessor;

import com.mikealbert.batch.listeners.StepExecutionListenerCtxInjector;
import com.mikealbert.util.ObjectUtils;

public class RecordIdDerivingProcessor<T> implements ItemProcessor<T, T> {
	
	@Resource StepExecutionListenerCtxInjector ctxInjector;
	
	@Override
	public T process(T targetItem) throws Exception {
		Long recId = ctxInjector.getJobExecutionCtx().getLong("recordId");
		recId ++;
		if(targetItem instanceof List){
			ObjectUtils.setProperty(((List)targetItem).get(0),"recordId",recId);
		}else{
			ObjectUtils.setProperty(targetItem,"recordId",recId);
		}
		//ObjectUtils.setProperty(targetItem,"recordId",recId);
		ctxInjector.getJobExecutionCtx().put("recordId", recId);
		
		return targetItem;
	}
}
