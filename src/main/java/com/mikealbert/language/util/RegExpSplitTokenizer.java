package com.mikealbert.language.util;

public class RegExpSplitTokenizer implements LanguageTokenizer {
	private String pattern;
	
	public RegExpSplitTokenizer(String pattern){
		this.pattern = pattern;
	}
	
	@Override
	public String[] tokenizeString(String inputString) {
		String[] splitString =  inputString.split(this.pattern);
		
		return splitString;
	}

}
