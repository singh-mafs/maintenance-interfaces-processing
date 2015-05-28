package com.mikealbert.batch.processors;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import com.mikealbert.util.ObjectUtils;

public class DetailLineIdDerivingProcessor<T> implements ItemProcessor<T, T> {

	@Override
	public T process(T targetItem) throws Exception {
		List details = (List) ObjectUtils.getProperty(targetItem, "details");
		Long lineId = 1L;
		
		for(Object detail : details){
			ObjectUtils.setProperty(detail, "lineId", lineId);
			lineId ++;
		}
		
		return targetItem;
	}
}
