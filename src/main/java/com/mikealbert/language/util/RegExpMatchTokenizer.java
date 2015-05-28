package com.mikealbert.language.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpMatchTokenizer implements LanguageTokenizer {
	private String pattern;
	private boolean upperCase = false;
	
	public RegExpMatchTokenizer(String pattern){
		this.pattern = pattern;
	}
	
	public RegExpMatchTokenizer(String pattern, boolean upperCase){
		this.pattern = pattern;
		this.upperCase = upperCase;
	}
	
	@Override
	public String[] tokenizeString(String inputString) {
		Pattern tokenPattern = Pattern.compile(pattern);
		List<String> tokenList = new ArrayList<String>();
		String tokenVal;
		// use a matcher to tokenize the strings
		Matcher tokenMatcher = tokenPattern.matcher(inputString);
	    while (tokenMatcher.find()) {
	    	tokenVal = tokenMatcher.group();
	    	if(upperCase){
	    		tokenList.add(tokenVal.toUpperCase());
	    	}else{
	    		tokenList.add(tokenVal);
	    	}
	    }
	
		return tokenList.toArray(new String[tokenList.size()]);
	}

}
