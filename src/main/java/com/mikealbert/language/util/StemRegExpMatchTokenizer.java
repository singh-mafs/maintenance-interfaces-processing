package com.mikealbert.language.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StemRegExpMatchTokenizer implements LanguageTokenizer {
	private String pattern;
	private boolean upperCase = false;
	
	public StemRegExpMatchTokenizer(String pattern){
		this.pattern = pattern;
	}
	
	public StemRegExpMatchTokenizer(String pattern, boolean upperCase){
		this.pattern = pattern;
		this.upperCase = upperCase;
	}
	
	@Override
	public String[] tokenizeString(String inputString) {
		Pattern tokenPattern = Pattern.compile(pattern);
		List<String> tokenList = new ArrayList<String>();
		LanguageUtils lang = new LanguageUtils();

		String tokenVal;
		// use a matcher to tokenize the strings
		Matcher tokenMatcher = tokenPattern.matcher(inputString);
	    while (tokenMatcher.find()) {
	    	tokenVal = tokenMatcher.group();
	    	tokenVal = lang.stemming(tokenVal);
			
	    	if(upperCase){
	    		tokenList.add(tokenVal.toUpperCase());
	    	}else{
	    		tokenList.add(tokenVal);
	    	}
	    }
	    
	
		return tokenList.toArray(new String[tokenList.size()]);
	}

}
