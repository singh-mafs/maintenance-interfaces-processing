package com.mikealbert.batch.linetokenizers;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

public class RemoveQuoteCharacterDelimitedLineTokenizer extends DelimitedLineTokenizer {
	@Override
	protected boolean isQuoteCharacter(char c) {
		return false;
	}
}
