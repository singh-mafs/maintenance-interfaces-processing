package com.mikealbert.batch.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.util.MALUtilities;

public class ValvolineInstantStoreLocationFieldSetMapper implements FieldSetMapper<StoreLocationVO> {
	
	private static String STORE_OPEN_WEEKDAY = "STORE_OPEN_WEEKDAY";
	private static String STORE_CLOSE_WEEKDAY = "STORE_CLOSE_WEEKDAY";
	private static String STORE_OPEN_SAT = "STORE_OPEN_SAT";
	private static String STORE_CLOSE_SAT = "STORE_CLOSE_SAT";
	private static String STORE_OPEN_SUN = "STORE_OPEN_SUN";
	private static String STORE_CLOSE_SUN = "STORE_CLOSE_SUN";
	
	public StoreLocationVO mapFieldSet(FieldSet fieldSet) throws BindException {
		StoreLocationVO vo = new StoreLocationVO();
		
		vo.setStoreCode(fieldSet.readString("Store #"));
		vo.setStoreName("Valvoline Instant Oil Change");
		vo.setAddressLine1(fieldSet.readString("Address"));
		vo.setZipCode(fieldSet.readString("Zip Code"));
		vo.setCity(fieldSet.readString("City"));
		vo.setStateProv(fieldSet.readString("State"));
		vo.setTelephoneNumber(fieldSet.readString("Store Phone"));
		
		if(STORE_OPEN_WEEKDAY.equalsIgnoreCase(fieldSet.readString("Store Hours Parameter"))){
			vo.setWeekdayStartTime(MALUtilities.convertTimeTo12hrsFormat(fieldSet.readString("Time").equalsIgnoreCase("0") ? "0000" :  fieldSet.readString("Time")));
		}

		if(STORE_CLOSE_WEEKDAY.equalsIgnoreCase(fieldSet.readString("Store Hours Parameter"))){
			vo.setWeekdayEndTime(MALUtilities.convertTimeTo12hrsFormat(fieldSet.readString("Time").equalsIgnoreCase("0") ? "0000" :  fieldSet.readString("Time")));
		}
		
		if(STORE_OPEN_SAT.equalsIgnoreCase(fieldSet.readString("Store Hours Parameter"))){
			vo.setSaturdayStartTime(MALUtilities.convertTimeTo12hrsFormat(fieldSet.readString("Time").equalsIgnoreCase("0") ? "0000" :  fieldSet.readString("Time")));
		}
		
		if(STORE_CLOSE_SAT.equalsIgnoreCase(fieldSet.readString("Store Hours Parameter"))){
			vo.setSaturdayEndTime(MALUtilities.convertTimeTo12hrsFormat(fieldSet.readString("Time").equalsIgnoreCase("0") ? "0000" :  fieldSet.readString("Time")));
		}
		
		
		if(STORE_OPEN_SUN.equalsIgnoreCase(fieldSet.readString("Store Hours Parameter"))){
			vo.setSundayStartTime(MALUtilities.convertTimeTo12hrsFormat(fieldSet.readString("Time").equalsIgnoreCase("0") ? "0000" :  fieldSet.readString("Time")));
		}
		
		if(STORE_CLOSE_SUN.equalsIgnoreCase(fieldSet.readString("Store Hours Parameter"))){
			vo.setSundayEndTime(MALUtilities.convertTimeTo12hrsFormat(fieldSet.readString("Time").equalsIgnoreCase("0") ? "0000" :  fieldSet.readString("Time")));
		}
		

		return vo;
	}

}
