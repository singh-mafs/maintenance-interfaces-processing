package com.mikealbert.batch.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import com.mikealbert.data.vo.StoreLocationVO;

public class ValvolineInstantStoreLocationFieldSetMapperTest {

	@Test
	public void mapFieldSetTest(){
		ValvolineInstantStoreLocationFieldSetMapper mapper = new ValvolineInstantStoreLocationFieldSetMapper();
		String[] tokens = new String[]{"010002","BLOOMINGTON","8602 Lyndale Ave South","Bloomington","MN","55420","952-884-1999","STORE_OPEN_WEEKDAY","1800"};
		String[] names = new String[]{"Store #","Store Name","Address","City","State","Zip Code","Store Phone","Store Hours Parameter","Time"};
		
		FieldSet fs = new DefaultFieldSet(tokens,names);
		
			try {
				StoreLocationVO storeLocationVO = mapper.mapFieldSet(fs);
				assertEquals(storeLocationVO.getStoreCode(), "010002");
				assertEquals(storeLocationVO.getStoreName(), "Valvoline Instant Oil Change");
				assertEquals(storeLocationVO.getAddressLine1(), "8602 Lyndale Ave South");
				assertEquals(storeLocationVO.getCity(), "Bloomington");
				assertEquals(storeLocationVO.getStateProv(), "MN");
				assertEquals(storeLocationVO.getZipCode(), "55420");
				assertEquals(storeLocationVO.getTelephoneNumber(), "952-884-1999");
				assertEquals(storeLocationVO.getWeekdayStartTime(), "06:00 PM");

			} catch (BindException e) {
				fail("Mapping Failed " + e.getMessage());
			}		
		}
}
