package com.mikealbert.batch.validators;

import static org.junit.Assert.*;

import org.apache.commons.validator.Validator;
import org.junit.Test;
import org.springframework.batch.item.validator.ValidationException;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.data.vo.VendorMaintCodeVO;

public class VendorMaintCodeValidatorTest {
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Test
	public void validatePartServiceCodeMissingTest(){
		VendorMaintCodeValidator Validator = new VendorMaintCodeValidator();
		VendorMaintCodeVO maintVO = new VendorMaintCodeVO();
				
		maintVO.setPartServiceCode("");
		try{
			Validator.validate(maintVO);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("partServiceCode is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
	public void validatePartServiceCodeMaxLengthTest(){
		VendorMaintCodeValidator Validator = new VendorMaintCodeValidator();
		VendorMaintCodeVO maintVO = new VendorMaintCodeVO();
				
		maintVO.setPartServiceCode("AAAAAAAAAAAAAAAAAAAAAAAAAAA");
		try{
			Validator.validate(maintVO);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("partServiceCode exceeds length 25"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
	public void validatePartServiceDescMissingTest(){
		VendorMaintCodeValidator Validator = new VendorMaintCodeValidator();
		VendorMaintCodeVO maintVO = new VendorMaintCodeVO();
				
		maintVO.setPartServiceDesc("");
		try{
			Validator.validate(maintVO);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("partServiceDesc is required"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
	public void validatePartServiceDescMaxLengthTest(){
		VendorMaintCodeValidator Validator = new VendorMaintCodeValidator();
		VendorMaintCodeVO maintVO = new VendorMaintCodeVO();
				
		maintVO.setPartServiceDesc("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		try{
			Validator.validate(maintVO);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("partServiceDesc exceeds length 80"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
	public void validateOperationCodeMissingTest(){
		VendorMaintCodeValidator Validator = new VendorMaintCodeValidator();
		VendorMaintCodeVO maintVO = new VendorMaintCodeVO();
				
		maintVO.setOperationCode("");
		try{
			Validator.validate(maintVO);
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
		VendorMaintCodeValidator Validator = new VendorMaintCodeValidator();
		VendorMaintCodeVO maintVO = new VendorMaintCodeVO();
				
		maintVO.setOperationCode("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		try{
			Validator.validate(maintVO);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("Operation code exceed length 1"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	@Test
	public void validateOperationCodeTest(){
		VendorMaintCodeValidator Validator = new VendorMaintCodeValidator();
		VendorMaintCodeVO maintVO = new VendorMaintCodeVO();
				
		maintVO.setOperationCode("Z");
		try{
			Validator.validate(maintVO);
			fail("Validation Succeeded but should not");
		}catch(ValidationException v){
			assertTrue("Validation Failed due to valid exception", true);
			assertTrue("Validation Failed with the correct message", v.getMessage().contains("Operation code must be A,M or D"));
		}catch(Exception e){
			fail("Validation Failed due to invalid exception");
		}
	}
	
	private VendorMaintCodeVO constructVendorMaintCodeVO(){
		VendorMaintCodeVO vo = new VendorMaintCodeVO();
		vo.setOperationCode("Code-Test");
		vo.setPartServiceCode("85142");
		vo.setPartServiceDesc("Description-test");
		
		return vo;
	}

}
