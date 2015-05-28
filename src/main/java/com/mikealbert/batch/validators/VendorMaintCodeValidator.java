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
import com.mikealbert.data.vo.VendorMaintCodeVO;
import com.mikealbert.util.MALUtilities;

@Component("vendorMaintCodeValidator")
public class VendorMaintCodeValidator implements Validator<VendorMaintCodeVO> {

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Resource protected MalMessage malMessage;
	
	public void validate(VendorMaintCodeVO value) throws ValidationException {
		List<String> validationErrors = new ArrayList<String>();
		HashMap<String,List<String>> fieldErrors = new HashMap<String,List<String>>();
		List<String> fieldErrorMsgs = new ArrayList<String>();
		
		logger.info("Vendor Maintenance Code Validation - "  + value.getPartServiceCode() + " " + value.getPartServiceDesc());
		
		String errMsg = null;
		//TODO: use messages file to get messages
		if(MALUtilities.isEmpty(value.getPartServiceCode())){
			errMsg = malMessage.getMessage("required.field", "Part Service Code ");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getPartServiceCode().length() > 25){
				errMsg = malMessage.getMessage("exceeds.length", "Part Service Code ","25");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("partServiceCode", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
		
		if(MALUtilities.isEmpty(value.getPartServiceDesc())){	
			errMsg = malMessage.getMessage("required.field", "Part Service Desc ");
			validationErrors.add(errMsg);
			fieldErrorMsgs.add(errMsg);
		}else{
			if(value.getPartServiceDesc().length() > 80){
				errMsg = malMessage.getMessage("exceeds.length", "Part Service Desc ","80");
				validationErrors.add(errMsg);
				fieldErrorMsgs.add(errMsg);
			}
		}
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("partServiceDesc", fieldErrorMsgs);
			fieldErrorMsgs = new ArrayList<String>();
		}
				
		if(MALUtilities.isEmpty(value.getOperationCode())){	
			errMsg = malMessage.getMessage("required.field", "Operation Code");
			validationErrors.add("Operation Code is required");
			fieldErrorMsgs.add("Operation Code is required");
		}else{
			if(value.getOperationCode().length() >1){
				errMsg = malMessage.getMessage("exceeds.length", "Operation Code","1");
			    validationErrors.add("Operation Code exceed length 1");	
			    fieldErrorMsgs.add("Operation Code exceed length 1");
			}
			if(!value.getOperationCode().matches("^[AM|am]{1}$")){
				errMsg = malMessage.getMessage("must.matchWith", "Operation Code","A OR M");
				validationErrors.add("Operation Code and should be A or M ");
				fieldErrorMsgs.add("Operation Code must be A OR M");
			}
		}
		
		if(fieldErrorMsgs.size() > 0){
			fieldErrors.put("operationCode", fieldErrorMsgs);
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
