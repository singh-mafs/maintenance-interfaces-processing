package com.mikealbert.batch.readers;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.mikealbert.util.ObjectUtils;

public class FixedCountMultiLineItemReader<T> implements ItemReader<T> {
	private int lineCount;

	private ItemReader<T> delegate;

	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		int i = 0;
		T record = null;
		
		for (T line = null; (line = this.delegate.read()) != null;) {
			// get back the initial
			if(record == null){
				record = line;
			}else{
				// use spring BeanWrapper to set any null properties to values returned that
				// are not null (add to the record with each pass)
				ObjectUtils.copyNonNullProperties(line,record);
			}
			
			// break out when we reach the end of the record
			i++;
			if(i >= lineCount){
				break;
			}
		}
		// finally return the completed record
		return record;
	}
	
	public ItemReader<T> getDelegate() {
		return delegate;
	}

	public void setDelegate(ItemReader<T> delegate) {
		this.delegate = delegate;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}	
	
	

	
	
}
