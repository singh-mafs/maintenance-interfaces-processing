package com.mikealbert.batch.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.stereotype.Component;

import com.mikealbert.batch.exceptions.MalFieldValidationException;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.common.MalMessage;
import com.mikealbert.data.util.DateTimeParsingHelper;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.util.MALUtilities;

@Component("storeLocationValidator")
public class StoreLocationValidator implements Validator<StoreLocationVO> {

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Resource protected MalMessage malMessage;
	
	public void validate(StoreLocationVO value) throws ValidationException {
		List<String> validationErrors = new ArrayList<String>();
		HashMap<String,List<String>> fieldErrors = new HashMap<String,List<String>>();
		List<String> fieldErrorMsgs = new ArrayList<String>();
		DateTimeParsingHelper dateHelper = new DateTimeParsingHelper();
		
		logger.info("Store Location Validation - "  + value.getStoreCode() + " " + value.getStoreName());
		
		String errMsg;
		
		//TODO: use messages file to get messages
		if(MALUtilities.isEmpty(value.getStoreCode())){	
			errMsg = malMessage.getMessage("required.field", "Store Code");
			validationErrors.add(errMsg);  
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getStoreCode().length() > 25){
				errMsg = malMessage.getMessage("exceeds.length", "Store Code","25");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("storeCode", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(MALUtilities.isEmpty(value.getStoreName())){	
			errMsg = malMessage.getMessage("required.field", "Store Name");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getStoreName().length() > 60){
				errMsg = malMessage.getMessage("exceeds.length", "Store Name","60");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
				
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("storeName", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(MALUtilities.isEmpty(value.getAddressLine1())){	
			errMsg = malMessage.getMessage("required.field", "Address Line1");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getAddressLine1().length() > 80){
				errMsg = malMessage.getMessage("exceeds.length", "Address Line1","80");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("addressLine1", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getAddressLine2())){	
			if(value.getAddressLine2().length() > 80){
				errMsg = malMessage.getMessage("exceeds.length", "Address Line2","80");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("addressLine2", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getAddressLine3())){	
			if(value.getAddressLine3().length() > 80){
				errMsg = malMessage.getMessage("exceeds.length", "Address Line3","80");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("addressLine3", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getAddressLine4())){	
			if(value.getAddressLine4().length() > 80){
				errMsg = malMessage.getMessage("exceeds.length", "Address Line4","80");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("addressLine4", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(MALUtilities.isEmpty(value.getTelephoneNumber())){	
			errMsg = malMessage.getMessage("required.field", "Telephone Number");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else if(!MALUtilities.isEmpty(value.getTelephoneNumber())){	
			if(value.getTelephoneNumber().length() > 14){
				errMsg = malMessage.getMessage("exceeds.length", "Telephone Number","14 and should be formatted as 000-000-0000");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
			
			if(value.getTelephoneNumber().length()<10){
				errMsg = malMessage.getMessage("below.length", "Telephone Number","10");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
			
			if(!value.getTelephoneNumber().matches("^\\(\\d{3}\\) \\d{3}( |-)?\\d{4}|^\\d{3}( |-)\\d{3}( |-)\\d{4}|^\\d{10}")){
				errMsg = malMessage.getMessage("must.matchWith", "Telephone Number format","000-000-0000 or (000) 000-0000 or 0000000000");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("telephoneNumber", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getFaxNumber())){	
			if(value.getFaxNumber().length() > 14){
				errMsg = malMessage.getMessage("exceeds.length", "Fax Number","14 and should be formatted as 000-000-0000");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
			if(!value.getFaxNumber().matches("^\\(\\d{3}\\) \\d{3}( |-)?\\d{4}|^\\d{3}( |-)\\d{3}( |-)\\d{4}|^\\d{10}")){
				errMsg = malMessage.getMessage("must.matchWith", "Fax Number format","000-000-0000 or (000) 000-0000 or 0000000000");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("faxNumber", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
        if(!MALUtilities.isEmpty(value.getEmailAddress())){	
			if(value.getEmailAddress().length() > 80){
				errMsg = malMessage.getMessage("exceeds.length", "Email Address","80");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
			if(!value.getEmailAddress().matches("^[_A-Za-z0-9-%\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$")){
				errMsg = malMessage.getMessage("must.matchWith", "Email Address format","valid format");
				validationErrors.add("Email Address must be a valid format");
				fieldErrorMsgs.add("Email Address must be a valid format");
			}
		}		
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("emailAddress", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
        
		if(!MALUtilities.isEmpty(value.getCountryCode()) && value.getCountryCode().length() > 80) {
			errMsg = malMessage.getMessage("exceeds.length", "countryCode", "80");
		    validationErrors.add(errMsg);
		    fieldErrorMsgs.add(errMsg);			
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("countryCode", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(MALUtilities.isEmpty(value.getZipCode())){
			errMsg = malMessage.getMessage("required.field", "Zip Code");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getZipCode().length() >10){
				errMsg = malMessage.getMessage("exceeds.length", "Zip Code","5  and should be formatted as 00000-0000");
			    validationErrors.add(errMsg);
			    fieldErrorMsgs.add(errMsg);
			}
			
			if(!MALUtilities.isEmpty(value.getCountryCode()) && value.getCountryCode().equalsIgnoreCase("CN")) {
				if(!value.getZipCode().matches("^\\D\\d\\D\\s\\d\\D\\d$")) {
					errMsg = malMessage.getMessage("must.matchWith", "Zip Code format" ,"[A-Z][0-9][A-Z] [0-9][A-Z][0-9]");
					validationErrors.add("Zip code must be formatted [A-Z][0-9][A-Z] [0-9][A-Z][0-9]");
					fieldErrorMsgs.add("Zip code must be formatted [A-Z][0-9][A-Z] [0-9][A-Z][0-9]");					
				}
			} else {
				// validation checking on USA by default
				if(!value.getZipCode().matches("^\\d{5}(-\\d{4})?$")){
					errMsg = malMessage.getMessage("must.matchWith", "Zip Code format" ,"00000-0000");
					validationErrors.add("Zip code must be formatted 00000-0000");
					fieldErrorMsgs.add("Zip code must be formatted 00000-0000");
				}
			}
		}
	
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("zipCode", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(MALUtilities.isEmpty(value.getStateProv())){	
			errMsg = malMessage.getMessage("required.field", "State Prov");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getStateProv().length() > 2){
				errMsg = malMessage.getMessage("exceeds.length", "State Prov","2 and should be formatted as AA");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
			if(!value.getStateProv().matches("^[A-Z]{2}$")){
				errMsg = malMessage.getMessage("must.matchWith", "State Prov format" ,"character value as AA");
				validationErrors.add("stateProv can have only character value and should be formatted as AA");
				fieldErrorMsgs.add("stateProv can have only character value and should be formatted as AA");
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("stateprov", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getCountyCode())){
			if(value.getCountyCode().length() >3){
				errMsg = malMessage.getMessage("exceeds.length", "County Code","3 and should be formatted as 000");
			    validationErrors.add(errMsg);
			    fieldErrorMsgs.add(errMsg);
			}
			if(!value.getCountyCode().matches("^[0-9]{3}$")){
				errMsg = malMessage.getMessage("must.matchWith", "County Code format" ,"000");
				validationErrors.add("County code must be formatted 000");
				fieldErrorMsgs.add("County code must be formatted 000");
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("countycode", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getClearanceInFeet())){	
			if(value.getClearanceInFeet() > 99){
				errMsg = malMessage.getMessage("must.lessThen", "Clearance in Feet should be a whole number and" ,"99");
				validationErrors.add("Clearance In Feet must be a whole number less than 99");
				fieldErrorMsgs.add("Clearance In Feet must be a whole number less than 99");
			}
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("clearanceInFeet", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getNumberOfBays())){
			if(value.getNumberOfBays() > 99){
				errMsg = malMessage.getMessage("must.lessThen", "Number Of Bays should be a whole number and" ,"100");
				validationErrors.add("Number Of Bays must be a whole number less than 100");
				fieldErrorMsgs.add("Number Of Bays must be a whole number less than 100");
			}
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("numberOfBays", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(MALUtilities.isEmpty(value.getCity())){	
			errMsg = malMessage.getMessage("required.field", "City");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getCity().length() >80){
				errMsg = malMessage.getMessage("exceeds.length", "City","80");
			    validationErrors.add(errMsg);
			    fieldErrorMsgs.add(errMsg);
			}
		}
		
		if(MALUtilities.isEmpty(value.getOperationCode())){
			errMsg = malMessage.getMessage("required.field", "Operation Code");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getOperationCode().length() >1){
				errMsg = malMessage.getMessage("exceeds.length", "Operation Code","1");
			    validationErrors.add(errMsg);	
			    fieldErrorMsgs.add(errMsg);	
			}
			if(!value.getOperationCode().matches("^[AMD|amd]{1}$")){
				errMsg = malMessage.getMessage("must.matchWith", "Operation Code format","A,M or D");
				validationErrors.add("Operation code must be A,M or D");
				fieldErrorMsgs.add("Operation code must be A,M or D");
			}
		}	
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("operationcode", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getWeekdayStartTime()) && !dateHelper.isDateString(value.getWeekdayStartTime())){
			errMsg = malMessage.getMessage("must.matchWith", "Weekday Start Time format","valid time format");
			validationErrors.add("Weekday Start Time must be a valid time format ");
			fieldErrorMsgs.add("Weekday Start Time must be a valid time format");
		}
		if(!MALUtilities.isEmpty(value.getWeekdayStartTime()) && value.getWeekdayStartTime().length() >12){
			errMsg = malMessage.getMessage("exceeds.length", "Weekday Start Time ","12 and should be formatted as valid time format");
		    validationErrors.add(errMsg);
		    fieldErrorMsgs.add(errMsg);
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("weekdayStartTime", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getWeekdayEndTime()) && !dateHelper.isDateString(value.getWeekdayEndTime())){
			errMsg = malMessage.getMessage("must.matchWith", "Weekday End Time format","valid time format");
			validationErrors.add("Weekday End Time must be a valid time format ");
			fieldErrorMsgs.add("Weekday End Time must be a valid time format");
		}
		if(!MALUtilities.isEmpty(value.getWeekdayEndTime()) && value.getWeekdayEndTime().length() >12){
			errMsg = malMessage.getMessage("exceeds.length", "Weekday End Time ","12 and should be formatted as valid time format");
		    validationErrors.add(errMsg);
		    fieldErrorMsgs.add(errMsg);
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("weekdayEndTime", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getSaturdayStartTime()) && !dateHelper.isDateString(value.getSaturdayStartTime())){
			errMsg = malMessage.getMessage("must.matchWith", "Saturday Start Time format","valid time format");
			validationErrors.add("Saturday Start Time must be a valid time format ");
			fieldErrorMsgs.add("Saturday Start Time must be a valid time format");
		}
		if(!MALUtilities.isEmpty(value.getSaturdayStartTime()) && value.getSaturdayStartTime().length() >12){
			errMsg = malMessage.getMessage("exceeds.length", "Saturday Start Time ","12 and should be formatted as valid time format");
		    validationErrors.add(errMsg);
		    fieldErrorMsgs.add(errMsg);
		}		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("saturdayStartTime", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		if(!MALUtilities.isEmpty(value.getSaturdayEndTime()) && !dateHelper.isDateString(value.getSaturdayEndTime())){
			errMsg = malMessage.getMessage("must.matchWith", "Saturday End Time format","valid time format");
			validationErrors.add("Saturday End Time must be a valid time format ");
			fieldErrorMsgs.add("Saturday End Time must be a valid time format");
		}
		if(!MALUtilities.isEmpty(value.getSaturdayEndTime()) && value.getSaturdayEndTime().length() >12){
			errMsg = malMessage.getMessage("exceeds.length", "Saturday End Time ","12 and should be formatted as valid time format");
		    validationErrors.add(errMsg);
		    fieldErrorMsgs.add(errMsg);
		}		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("saturdayEndTime", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		if(!MALUtilities.isEmpty(value.getSundayStartTime()) && !dateHelper.isDateString(value.getSundayStartTime())){
			errMsg = malMessage.getMessage("must.matchWith", "Sunday Start Time format","valid time format");
			validationErrors.add("Sunday Start Time must be a valid time format ");
			fieldErrorMsgs.add("Sunday Start Time must be a valid time format");
		}
		if(!MALUtilities.isEmpty(value.getSundayStartTime()) && value.getSundayStartTime().length() >12){
			errMsg = malMessage.getMessage("exceeds.length", "Sunday Start Time ","12 and should be formatted as valid time format");
		    validationErrors.add(errMsg);
		    fieldErrorMsgs.add(errMsg);
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("sundayStartTime", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(!MALUtilities.isEmpty(value.getSundayEndTime()) && !dateHelper.isDateString(value.getSundayEndTime())){
			errMsg = malMessage.getMessage("must.matchWith", "Sunday End Time format","valid time format");
			validationErrors.add("Sunday End Time must be a valid time format ");
			fieldErrorMsgs.add("Sunday End Time must be a valid time format");
		}
		if(!MALUtilities.isEmpty(value.getSundayEndTime()) && value.getSundayEndTime().length() >12){
			errMsg = malMessage.getMessage("exceeds.length", "Sunday End Time ","12 and should be formatted as valid time format");
		    validationErrors.add(errMsg);
		    fieldErrorMsgs.add(errMsg);
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("sundayEndTime", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(validationErrors.size() > 0){
			throw new MalFieldValidationException(formatValidaionMessage(validationErrors),fieldErrors);
		}
		
	}
	
	private String formatValidaionMessage(List<String> validaionErrors){
		// TODO: record identifier; error codes and a way to link to the original record
		StringBuffer errorMsg = new StringBuffer();
		errorMsg.append("Validaion Error(s) Occured : ");
		errorMsg.append(System.getProperty("line.separator"));
		for(String validationError : validaionErrors){
			errorMsg.append(validationError);
			errorMsg.append(System.getProperty("line.separator"));
		}
		return errorMsg.toString();
	}
	
}
