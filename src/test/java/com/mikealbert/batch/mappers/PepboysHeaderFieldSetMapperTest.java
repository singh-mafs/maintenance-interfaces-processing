package com.mikealbert.batch.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceHeaderVO;

public class PepboysHeaderFieldSetMapperTest {

	@Test
	public void mapFieldSetTest(){
		PepboysHeaderFieldSetMapper pepboysHeaderFieldSetMapperapper = new PepboysHeaderFieldSetMapper();
		
		// Pepboys Field Names
		String[] names = new String[]{"HEADER-RECORD"};
		
		// Pepboys Sample Data
		String[] tokens = new String[]{"H"};
		
		
		FieldSet fieldSet = new DefaultFieldSet(tokens,names);
		
		try {
			VendorInvoiceHeaderVO vendorInvoiceHeader = pepboysHeaderFieldSetMapperapper.mapFieldSet(fieldSet);
			
			assertEquals(vendorInvoiceHeader.getRecordType(), "H");
		
		} catch (BindException e) {
			fail("Mapping Failed " + e.getMessage());
		}		
	}
}
