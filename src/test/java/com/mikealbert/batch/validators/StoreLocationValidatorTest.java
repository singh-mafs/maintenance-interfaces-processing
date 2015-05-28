package com.mikealbert.batch.validators;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.batch.item.validator.ValidationException;

import com.mikealbert.data.vo.StoreLocationVO;

public class StoreLocationValidatorTest {	
	@Test
	public void validateSuccessTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		try{
			validator.validate(loc);
			assertTrue("Validation Succeeded", true);
		}catch(ValidationException v){
			fail("Validation Failed");
		}catch(Exception e){
			fail("Validation Failed");
		}
	}
	
	@Test
	public void validateAddressLine1MissingTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		loc.setAddressLine1("");
		
		try{
			validator.validate(loc);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("addressLine1 is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
	public void validateAddressLine1MaxLengthTest(){
			StoreLocationValidator validator = new StoreLocationValidator();
			StoreLocationVO loc = constructStoreLocationVO();
     		loc.setAddressLine1("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		
		try{
			validator.validate(loc);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("addressLine1 exceeds length 80"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}	
		
	@Test
	public void validateAddressLine1Test(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		loc.setAddressLine1("AAAAA");
		validator.validate(loc);
	    assertTrue(true);
	}
	
	@Test
	public void validateStoreCodeMissingTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		loc.setStoreCode("");
		
		try{
			validator.validate(loc);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("storeCode is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
    }
	
	@Test
	public void validateStoreCodeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setStoreCode("010101");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	} 
	
	@Test
    public void validateStoreCodeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setStoreCode("0101010101010101010101010101010");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("storeCode exceeds length 25"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateStoreNameMissingTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		loc.setStoreName("");
		
		try{
			validator.validate(loc);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("storeName is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
    public void validateStoreNameMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setStoreName("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("storeName exceeds length 60"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateStoreNameTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setStoreCode("AAAAAAAAAAAAAA");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	
	@Test
    public void validateTelephoneNumberMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setTelephoneNumber("123-123-1234-1234");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("telephoneNumber exceeds length 14"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateTelephoneNumberTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setTelephoneNumber("123-123-1234");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	
	@Test
    public void validatefaxNumberMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setFaxNumber("123-123-1234-1234");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("faxNumber exceeds length 14"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validatefaxNumberTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setFaxNumber("123-123-1234");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	
	@Test
	public void validateEmailAddress(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setEmailAddress("ravic@mikealbert.com");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	} 

	@Test
	public void validateZipCodeMissingTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		loc.setZipCode("");
		
		try{
			validator.validate(loc);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("Zip code is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
    public void validateZipCodeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setZipCode("123-123-1234-1234");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("zipcode exceeds length 5"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateZipCodeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setZipCode("12312-3123");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	} 

	@Test
	public void validateStateProvMissingTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		loc.setStateProv("");
		
		try{
			validator.validate(loc);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("StateProv is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
    public void validateStateProvMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setStateProv("XXX");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("stateprov exceeds length 2"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateStateProvTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setStateProv("XX");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	} 
	
	@Test
    public void validateCountyCodeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setCountyCode("1234");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("countycode exceeds length 3"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateCountyCodeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setCountyCode("123");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	} 

	@Test
	public void validateOperationCodeMissingTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
		loc.setOperationCode("");
		
		try{
			validator.validate(loc);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("Operation code is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
    public void validateOperationCodeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setOperationCode("AA");
	
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("operationcode exceeds length 1"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateOperationCodeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setOperationCode("A");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	} 
	
	@Test
    public void validateWeekdayStartTimeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setWeekdayStartTime("wrong time formate");
	    assertTrue("Validation succeed",true);
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("Weekday Start Time must be a valid time format"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
    public void validateWeekdayEndTimeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setWeekdayEndTime("wrong time formate");
	    assertTrue("Validation succeed",true);
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("Weekday End Time must be a valid time format"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateWeekdayEndTimeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setWeekdayEndTime("1800");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	
	@Test
    public void validateSaturdayStartTimeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSaturdayStartTime("wrong time formate");
	    assertTrue("Validation succeed",true);
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("Saturday Start Time must be a valid time format"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateSaturdayStartTimeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSaturdayStartTime("1800");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	
	@Test
    public void validateSaturdayEndTimeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSaturdayEndTime("wrong time formate");
	    assertTrue("Validation succeed",true);
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("Saturday End Time must be a valid time format"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateSaturdayEndTimeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSaturdayEndTime("1800");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	
	@Test
    public void validateSundayStartTimeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSundayStartTime("wrong time formate");
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("Sunday Start Time must be a valid time format"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateSundayStartTimeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSundayStartTime("1800");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	
	@Test
    public void validateSundayEndTimeMaxLengthTest(){
    	StoreLocationValidator validator = new StoreLocationValidator();
    	StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSundayEndTime("this is not a time");
	    try{
		    validator.validate(loc);
		    fail("Validation Succeeded but should not");
	    }catch(ValidationException v){
		    assertTrue("Validation Failed due to valid exception", true);
		    assertTrue("Validation Failed with the correct message", v.getMessage().contains("Sunday End Time must be a valid time format"));
	    }catch(Exception e){
		    fail("Validation Failed due to invalid exception");
	    }
	}
	
	@Test
	public void validateSundayEndTimeTest(){
		StoreLocationValidator validator = new StoreLocationValidator();
		StoreLocationVO loc = constructStoreLocationVO();
	    loc.setSundayEndTime("1800");
	    validator.validate(loc);
        assertTrue("Validation succeed",true);
	}
	private StoreLocationVO constructStoreLocationVO(){
		StoreLocationVO vo = new StoreLocationVO();
		vo.setStoreCode("Test Code-2");
		vo.setStoreName("Test Vendor Name");
		vo.setTelephoneNumber("952-884-1999");
		vo.setAddressLine1("102-08 156th Road");
		vo.setCity("Howard Beach");
		vo.setStateProv("NY");
		vo.setZipCode("11414");
		vo.setOperationCode("A");
		
		return vo;
	}
	
	

}
