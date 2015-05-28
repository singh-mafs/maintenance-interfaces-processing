package com.mikealbert.batch.exceptions;

import java.util.HashMap;
import java.util.List;

import org.springframework.batch.item.validator.ValidationException;

public class MalFieldValidationException extends ValidationException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String,List<String>> fieldExceptions;
	
	public MalFieldValidationException(String message, Throwable cause, HashMap<String,List<String>> fieldExceptions) {
		super(message, cause);
		this.fieldExceptions = fieldExceptions;
	}
	
	public MalFieldValidationException(String message, HashMap<String,List<String>> fieldExceptions) {
		super(message);
		this.fieldExceptions = fieldExceptions;
	}

	public HashMap<String,List<String>> getFieldExceptions() {
		return fieldExceptions;
	}

}
