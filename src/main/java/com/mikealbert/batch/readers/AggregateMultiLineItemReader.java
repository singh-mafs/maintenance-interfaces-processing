package com.mikealbert.batch.readers;

import java.util.HashMap;

import org.springframework.batch.item.ItemReader;

import com.mikealbert.util.MALUtilities;
import com.mikealbert.util.ObjectUtils;

public class AggregateMultiLineItemReader<T> implements ItemReader<T> {

	private ItemReader<T> itemReader;
	
	private String checkPropertyName;
	private String checkPropertyValue;
	private Boolean isFirstRecord;
	private HashMap<?, ?> checkPropertyToHelperClass;
	private T record = null;
	
	@Override
	public T read() throws Exception {
		T retVal	= null;
		T line 		= null;
		
		// keep reading until we reach the end of the file ("null" fields)
		for (line = null; (line = this.itemReader.read()) != null;) {
			
			//TODO: change this to make it more generic
			String checkPropVal = (String)ObjectUtils.getProperty(line, checkPropertyName);
			
			// very first line being read in
			if (MALUtilities.isEmpty(checkPropertyValue)) {
				checkPropertyValue 	= checkPropVal;
				record 				= line;
				retVal 				= record;
				isFirstRecord		= true;
			} else {
				isFirstRecord = false;
				
				// Setting up the Invoice record for records coming in after the very first record
				if (MALUtilities.isEmpty(retVal)) {
					retVal = record;
					record = null;
				}
				
				// read (process) until we reach a new "check property"
				if(!checkPropVal.equalsIgnoreCase(checkPropertyValue)){
					checkPropertyValue = checkPropVal;
					
					// set the record to the new line (so can save it until the next read)
					record = line;
					break;
				}else{
					Object o = checkPropertyToHelperClass.get("combine");
					ObjectUtils.invokeMethod(ObjectUtils.getMethod(o, "invokeHelperMethod", Object.class, Object.class), o, line, retVal);
				}
			}
		}
		
		if (!MALUtilities.isEmpty(record) && MALUtilities.isEmpty(retVal) && !isFirstRecord) {
			// Allows for a single line invoice at the end of the file to pass through the reader
			retVal = record;
			record = null;
		} else if(!MALUtilities.isEmpty(record) && MALUtilities.isEmpty(retVal) && isFirstRecord) {
			// Stops a single line invoice file from creating two separate invoices inside the Exports file
			record = null;
		}
		
		return retVal;
	}
	

	public void setItemReader(ItemReader<T> itemReader) {
		this.itemReader = itemReader;
	}
	
	public void setCheckPropertyToHelperClass(HashMap<?, ?> checkPropertyToHelperClass) {
		this.checkPropertyToHelperClass = checkPropertyToHelperClass;
	}
	
	public void setCheckPropertyName(String checkPropertyName) {
		this.checkPropertyName = checkPropertyName;
	}
	
	public void setIsFirstRecord(Boolean isFirstRecord) {
		this.isFirstRecord = isFirstRecord;
	}
	
}
