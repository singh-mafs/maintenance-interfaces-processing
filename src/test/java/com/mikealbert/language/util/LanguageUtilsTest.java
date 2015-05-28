package com.mikealbert.language.util;

import org.junit.Assert;
import org.junit.Test;


public class LanguageUtilsTest {

	@Test
	public void jaccardSimilarityTest(){
		double confidence;
		LanguageUtils lang = new LanguageUtils();
		confidence = lang.jaccardSimilarity("153 West Squire Dr","147 West Squire Dr");

		Assert.assertEquals(confidence, 0.60, 0.10);
	}

	@Test
	public void jaccardSimilarityAlphaWordsOnlyTest(){
		double confidence;
		RegExpMatchTokenizer tokenizer = new RegExpMatchTokenizer("[A-z]+");
		LanguageUtils lang = new LanguageUtils();
		lang.setTokenizer(tokenizer);
		
		confidence = lang.jaccardSimilarity("153 West Squire Dr","147 West Squire Dr");

		Assert.assertEquals(confidence, 1.00, 0.00);
	}
	
	
	@Test
	public void jaccardSimilarityAlphaCaseInsensitiveTest(){
		double confidence;
		RegExpMatchTokenizer tokenizer = new RegExpMatchTokenizer("[A-z]+",true);
		LanguageUtils lang = new LanguageUtils();
		lang.setTokenizer(tokenizer);
		
		confidence = lang.jaccardSimilarity("particle filter, diesel","Diesel particle filter");

		Assert.assertEquals(confidence, 1.00, 0.00);
	}
	
	@Test
	public void jaccardSimilarityStemmingCaseInsensitiveTest(){
		double confidence;
		StemRegExpMatchTokenizer tokenizer = new StemRegExpMatchTokenizer("[A-z/]+",true);
		LanguageUtils lang = new LanguageUtils();
		lang.setTokenizer(tokenizer);
		
		confidence = lang.jaccardSimilarity("Labor to drill out and remove broken A/C bolt","Labor for drilling out and removing broken bolts");

		Assert.assertEquals(confidence, 0.70, 0.10);
	}
	
	
	@Test
	public void simpleStemmingTest(){
		LanguageUtils lang = new LanguageUtils();
		String stem = lang.stemming("Compressing");
	
		Assert.assertEquals("The value was not stemmed as expected" ,"compress" , stem);
	}
	
	@Test
	public void advancedStemmingTest(){
		LanguageUtils lang = new LanguageUtils();
		String stem = lang.stemming("A/C");
		
		Assert.assertEquals("The value was not stemmed as expected" ,"a/c" , stem);
	}
	
	@Test
	public void advanced2StemmingTest(){
		LanguageUtils lang = new LanguageUtils();
		String stem = lang.stemming("removing");
		
		Assert.assertEquals("The value was not stemmed as expected" ,"remove" , stem);
	}
	
}

