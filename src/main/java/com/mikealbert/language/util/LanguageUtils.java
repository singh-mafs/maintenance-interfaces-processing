package com.mikealbert.language.util;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.util.CoreMap;


public class LanguageUtils {

	private LanguageTokenizer tokenizer;
	
	/*
	 * Jaccard Similarity is a similarity function which is calculated by 
	 * first tokenizing the strings into sets and then taking the ratio of
	 * (weighted) intersection to their union 
	 */	
	public double jaccardSimilarity(String similar1, String similar2){
		LanguageTokenizer internalTokenizer = null;
		if(tokenizer == null){
			internalTokenizer = new RegExpSplitTokenizer("\\s+");
		}else{
			internalTokenizer = tokenizer;
		}
		
		HashSet<String> h1 = new HashSet<String>();
		HashSet<String> h2 = new HashSet<String>();
		
		for(String s: internalTokenizer.tokenizeString(similar1)){
		h1.add(s);		
		}
		//System.out.println("h1 "+ h1);
		for(String s: internalTokenizer.tokenizeString(similar2)){
		h2.add(s);		
		}
		//System.out.println("h2 "+ h2);
		
		int sizeh1 = h1.size();
		//Retains all elements in h3 that are contained in h2 ie intersection
		h1.retainAll(h2);
		//h1 now contains the intersection of h1 and h2
		//System.out.println("Intersection "+ h1);
		
			
		h2.removeAll(h1);
		//h2 now contains unique elements
		//System.out.println("Unique in h2 "+ h2);
		
		//Union 
		int union = sizeh1 + h2.size();
		int intersection = h1.size();
		
		return (double)intersection/union;
		
	}
	
	public String stemming(String inputString){
		String retVal = null;

		Morphology x  = new Morphology();
		retVal =  x.stem(inputString);

		return retVal;
	}
	

	public LanguageTokenizer getTokenizer() {
		return tokenizer;
	}

	public void setTokenizer(LanguageTokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
}
