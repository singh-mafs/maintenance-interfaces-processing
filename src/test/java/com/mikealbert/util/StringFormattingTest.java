package com.mikealbert.util;

import org.junit.Assert;
import org.junit.Test;

public class StringFormattingTest {

	@Test
	public void padUnitNumberTest(){
		String rawUnitNo = "436367";
		String paddedUnitNo;
		
		if(MALUtilities.isNotEmptyString(rawUnitNo) && MALUtilities.isNumber(rawUnitNo) && rawUnitNo.length()>5){
			paddedUnitNo = String.format("%1$" + 8 + "s", rawUnitNo).replace(" ","0");
		}else
		{
			// TODO: your dumb
			paddedUnitNo = rawUnitNo;
		}
		
		Assert.assertTrue(paddedUnitNo.length() == 8);
		
	}
	
}

