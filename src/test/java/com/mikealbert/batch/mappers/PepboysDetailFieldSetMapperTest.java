package com.mikealbert.batch.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;

public class PepboysDetailFieldSetMapperTest {

	@Test
	public void mapFieldSetTest(){
		PepboysDetailFieldSetMapper pepboysDetailFieldSetMapper = new PepboysDetailFieldSetMapper();
		
		// Pepboys Field Names
		String[] names = new String[]{"EDI-REC-TYPE"};
		
		// Pepboys Sample Data
		String[] tokens = new String[]{"D"};
		
		
		FieldSet fieldSet = new DefaultFieldSet(tokens,names);
		
		try {
			VendorInvoiceDetailsVO vendorInvoiceDetails = pepboysDetailFieldSetMapper.mapFieldSet(fieldSet);
			
			assertEquals(vendorInvoiceDetails.getRecordType(), "H");
		
		} catch (BindException e) {
			fail("Mapping Failed " + e.getMessage());
		}		
	}
}
