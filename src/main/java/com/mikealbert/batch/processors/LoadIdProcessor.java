package com.mikealbert.batch.processors;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemProcessor;

import com.mikealbert.batch.listeners.StepExecutionListenerCtxInjector;
import com.mikealbert.util.ObjectUtils;

public class LoadIdProcessor<T> implements ItemProcessor<T, T> {
	
	@Resource StepExecutionListenerCtxInjector ctxInjector;
	
	@Override
	public T process(T targetItem) throws Exception {
		Long loadId = ctxInjector.getJobExecutionCtx().getLong("loadId");

		ObjectUtils.setProperty(targetItem,"loadId",loadId);

		return targetItem;
	}
}
