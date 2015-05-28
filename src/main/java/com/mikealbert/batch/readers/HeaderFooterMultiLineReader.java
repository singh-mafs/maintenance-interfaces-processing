package com.mikealbert.batch.readers;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class HeaderFooterMultiLineReader<T> implements ItemReader<T> {
	//TODO: this would be used with PatternMatchingCompositeLineTokenizer
	// and be passed different VO types / subtypes map using Dozer or BeanWrapper
	//TODO: I still need to work out how to determine "breaks" in records off of types
	//returned by a tokenizer maybe I should explore PatternMatchingCompositeLineMatcher more
	private String headerType_qualifier;
	private String footerType_qualifier;
	
	private ItemReader<T> headerDelegate;
	private ItemReader<T> detailDelegate;
	
	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeaderTypeQualifier() {
		return headerType_qualifier;
	}

	public void setHeaderTypeQualifier(String headerType_qualifier) {
		this.headerType_qualifier = headerType_qualifier;
	}

	public String getFooterTypeQualifier() {
		return footerType_qualifier;
	}

	public void setFooterTypeQualifier(String footerType_qualifier) {
		this.footerType_qualifier = footerType_qualifier;
	}

	public ItemReader<T> getHeaderDelegate() {
		return headerDelegate;
	}

	public void setHeaderDelegate(ItemReader<T> headerDelegate) {
		this.headerDelegate = headerDelegate;
	}

	public ItemReader<T> getDetailDelegate() {
		return detailDelegate;
	}

	public void setDetailDelegate(ItemReader<T> detailDelegate) {
		this.detailDelegate = detailDelegate;
	}


	
	
	
}
