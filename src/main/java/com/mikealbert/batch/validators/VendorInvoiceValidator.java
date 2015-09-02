package com.mikealbert.batch.validators;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mikealbert.batch.exceptions.MalFieldValidationException;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.common.MalMessage;
import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

@Component("vendorInvoiceValidator")
public class VendorInvoiceValidator implements Validator<VendorInvoiceHeaderVO> {

  private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
  
  @Resource protected MalMessage malMessage;
  
  @Value("${coupons.code}")
  private	String	COUPONS_CODE;  
  
  public void validate(VendorInvoiceHeaderVO value) throws ValidationException {
    List<String> validationErrors = new ArrayList<String>();
    HashMap<String,List<String>> fieldErrors = new HashMap<String,List<String>>();
    List<String> fieldErrorMsgs = new ArrayList<String>();
    
    logger.info("Vendor Invoice Validation - "  + value.getParentProvider() + " " + value.getDocNumber());
    
    String errMsg = null;

    //header
    //Unit_Number + VIN
    if(MALUtilities.isEmptyString(value.getUnitNo()) && MALUtilities.isEmptyString(value.getVin())){
      errMsg = malMessage.getMessage("required.field", "Unit_Number or VIN");
      validationErrors.add(errMsg);
      fieldErrorMsgs.add(errMsg);
      if(fieldErrorMsgs.size() > 0){
        fieldErrors.put("Unit_Number", fieldErrorMsgs);
        fieldErrors.put("VIN", fieldErrorMsgs);
        fieldErrorMsgs = new ArrayList<String>();
      }
    }else{
    // Unit_Number
      if(!MALUtilities.isEmptyString(value.getUnitNo())){
        if(value.getUnitNo().length() > 25){
          errMsg = malMessage.getMessage("exceeds.length", "Unit_Number","25");
          validationErrors.add(errMsg);
          fieldErrorMsgs.add(errMsg);
        }
      }
      if(fieldErrorMsgs.size() > 0){
        fieldErrors.put("Unit_Number", fieldErrorMsgs);
        fieldErrorMsgs = new ArrayList<String>();
      }
      
    // VIN
      if(!MALUtilities.isEmptyString(value.getVin())){
        if(value.getVin().length() > 25){
          errMsg = malMessage.getMessage("exceeds.length", "VIN","25");
          validationErrors.add(errMsg);
          fieldErrorMsgs.add(errMsg);
        }
      }
      if(fieldErrorMsgs.size() > 0){
        fieldErrors.put("VIN", fieldErrorMsgs);
        fieldErrorMsgs = new ArrayList<String>();
      }
    }
    
    // PO_Ref_Nbr
    if(!MALUtilities.isEmpty(value.getPoNbr())){
      if(value.getPoNbr().length() > 25){
        errMsg = malMessage.getMessage("exceeds.length", "PO_Ref_Nbr","25");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("PO_Ref_Nbr", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Store_Code
    if(MALUtilities.isEmpty(value.getStoreNbr())){
      errMsg = malMessage.getMessage("required.field", "Store_Code");
      validationErrors.add(errMsg);
      fieldErrorMsgs.add(errMsg);
    }else{
      if(value.getStoreNbr().length() > 25){
        errMsg = malMessage.getMessage("exceeds.length", "Store_Code","25");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Store_Code", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Doc_Type
    if(MALUtilities.isEmpty(value.getDocType())){
      errMsg = malMessage.getMessage("required.field", "Doc_Type");
      validationErrors.add(errMsg);
      fieldErrorMsgs.add(errMsg);
    }else{
      if(!value.getDocType().matches("^INV|CRD$")){
        errMsg = malMessage.getMessage("must.matchWith", "Doc_Type","INV or CRD");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Doc_Type", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Invoice_No
    if(MALUtilities.isEmpty(value.getDocNumber())){
      errMsg = malMessage.getMessage("required.field", "Invoice_No");
      validationErrors.add(errMsg);
      fieldErrorMsgs.add(errMsg);
    }else{
      if(value.getDocNumber().length() > 25){
        errMsg = malMessage.getMessage("exceeds.length", "Invoice_No","25");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Invoice_No", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Driver
    if(!MALUtilities.isEmptyString(value.getDriver())){
      if(value.getDriver().length() > 240){
        errMsg = malMessage.getMessage("exceeds.length", "Driver","240");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Driver", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Reference_Code
    if(!MALUtilities.isEmptyString(value.getVendorRef())){
      if(value.getVendorRef().length() > 80){
        errMsg = malMessage.getMessage("exceeds.length", "Reference_Code","80");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Reference_Code", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Doc Date
    if(!MALUtilities.isEmpty(value.getDocDate())){
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      sdf.setLenient(false); // Don't automatically convert invalid date.
      
      try {
        Date date = sdf.parse(value.getDocDate());
      } catch (ParseException e) {
        errMsg = malMessage.getMessage("must.matchWith", "Doc_Date format","MM/DD/YYYY");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Doc_Date", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }   
    
    // Start_Date - Required: Service Start Date in the following format 12/12/2013
    if(MALUtilities.isEmpty(value.getPlannedDate())){
      errMsg = malMessage.getMessage("required.field", "Start_Date ");
      validationErrors.add(errMsg);
      fieldErrorMsgs.add(errMsg);
    }else{
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      sdf.setLenient(false); // Don't automatically convert invalid date.
      
      try {
        Date date = sdf.parse(value.getPlannedDate());
      } catch (ParseException e) {
        errMsg = malMessage.getMessage("must.matchWith", "Start_Date format","MM/DD/YYYY");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Start_Date", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Odometer - Required: Vehicle Odometer Reading at time of Service.
    if(MALUtilities.isEmpty(value.getMileage())){
      errMsg = malMessage.getMessage("required.field", "Odometer ");
      validationErrors.add(errMsg);
      fieldErrorMsgs.add(errMsg);
    }else{
      if((!MALUtilities.isValidPositiveInteger(value.getMileage())) || value.getMileage().length() > 7){
        errMsg = malMessage.getMessage("must.lessThen", "Odometer should be a whole number and" ,"9999999");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Odometer", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Plate_Reg_Number - Optional License Plate / Vehicle Registration Number
    if(!MALUtilities.isEmptyString(value.getPlateNo())){
      if(value.getPlateNo().length() > 25){
        errMsg = malMessage.getMessage("exceeds.length", "Plate_Reg_Number ","25");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Plate_Reg_Number", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    // Original_Doc_No - Optional Needed for credit invoices; this would be the original doc number
    if(!MALUtilities.isEmpty(value.getOrigDocNo())){
      //Credit processing
      //Doc_Type nust be CRD if Original_Doc_No is populated
      if(!value.getDocType().equalsIgnoreCase("CRD")){
        errMsg = malMessage.getMessage("must.matchWith", "Doc_Type ","CRD when Original_Doc_No is present");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
      
      if(value.getOrigDocNo().length() > 25){
        errMsg = malMessage.getMessage("exceeds.length", "Original_Doc_No ","25");
        validationErrors.add(errMsg);
        fieldErrorMsgs.add(errMsg);
      }
    }
    if(fieldErrorMsgs.size() > 0){
      fieldErrors.put("Original_Doc_No", fieldErrorMsgs);
      fieldErrorMsgs = new ArrayList<String>();
    }
    
    //details
    if(value.getDetails().size() == 0){
      errMsg = malMessage.getMessage("required.field", "Invoice Detail Lines - count(0) ");
      validationErrors.add(errMsg);
      fieldErrorMsgs.add(errMsg);
      
      if(fieldErrorMsgs.size() > 0){
        fieldErrors.put("Details", fieldErrorMsgs);
        fieldErrorMsgs = new ArrayList<String>();
      }
    }else{
      int cntOfTaxLines = 0; 
      int cntOfDiscountLines = 0;
      
      //In the future we don't care whether dollar amounts or qtys are  positive or negative, so I'm changing this now.
      for(int i = 1; i <= value.getDetails().size(); i++){
        VendorInvoiceDetailsVO detail = value.getDetails().get(i-1);
        
        //Tax_Amount - Optional Cost of Tax (Per Invoice)
        if(!MALUtilities.isEmpty(detail.getTaxAmount()) 
            && MALUtilities.isValidDecimalWMinMaxPrecision(detail.getTaxAmount(), 0, 2) 
            && !MALUtilities.stringMatchesBigDecimal(detail.getTaxAmount(), new BigDecimal(0))){
          
          cntOfTaxLines++;
          //validate that there is only one line with a Tax Amount on it; create an error if there is
          // more than 1.
          if(cntOfTaxLines > 1){
            errMsg = malMessage.getMessage("decode.multipleMatchesFound.msg","Tax_Amount - Doc No " + value.getDocNumber());
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
          }
          if(!MALUtilities.isValidDecimalWMinMaxPrecision(detail.getTaxAmount(), 0, 2)){
              errMsg = malMessage.getMessage("must.matchWith","Tax_Amount - Detail line " + i, "0000.00");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
          }
          
          if(detail.getTaxAmount().length() > 10){
            errMsg = malMessage.getMessage("exceeds.length", "Tax_Amount - Detail line " + i,"10");
            validationErrors.add(errMsg);
            fieldErrorMsgs.add(errMsg);
          }
          // TODO: we should not have a unit qty other than 1 or 0
          // TODO: we should not have a unit price
          // TODO: we should not have a excise tax
          // TODO: we should not have discount / rebate amount
        
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Tax_Amount"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }
        //If this line appears to follow the rules of an Invoice Level discount or rebate
        }else if(MALUtilities.isNotEmptyString(detail.getDiscRebateAmt()) 
            && MALUtilities.isValidDecimalWMinMaxPrecision(detail.getDiscRebateAmt(), 0, 2) 
            && !MALUtilities.stringMatchesBigDecimal(detail.getDiscRebateAmt(), new BigDecimal(0)) 
            && MALUtilities.isEmptyString(detail.getPartServiceCode())){
          
		  // exclude COUPONS from count
		  if(!COUPONS_CODE.equals(detail.getPartServiceCode())){
			cntOfDiscountLines++;
		  }
          
          if(cntOfDiscountLines > 1){
            errMsg = malMessage.getMessage("decode.multipleMatchesFound.msg","Disc_Rebate_Amount - Doc No " + value.getDocNumber());
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
          }
          
          if(!MALUtilities.isValidDecimalWMinMaxPrecision(detail.getDiscRebateAmt(), 0, 2)){
              errMsg = malMessage.getMessage("must.matchWith","Disc_Rebate_Amount - Detail line " + i, "0000.00");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
          }

          if(detail.getDiscRebateAmt().length() > 10){
            errMsg = malMessage.getMessage("exceeds.length", "Disc_Rebate_Amount - Detail line " + i,"10");
            validationErrors.add(errMsg);
            fieldErrorMsgs.add(errMsg);
          }
          
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Disc_Rebate_Amount"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }
        }else{
          // Part_Service_Code - Required; Unique "Code" for Invoicing/Billing for the Part or Service/Labor
          if(!MALUtilities.isEmpty(detail.getPartServiceCode())){
            if(detail.getPartServiceCode().length() > 25){
              errMsg = malMessage.getMessage("exceeds.length", "Part_Service_Code - Detail line " + i,"25");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
          }
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Part_Service_Code"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }

          // Part_Service_Desc - Required; Description of the Part or Service/Labor
          if(MALUtilities.isEmpty(detail.getPartServiceDesc())){
            errMsg = malMessage.getMessage("required.field", "Part_Service_Desc - Detail line " + i);
            validationErrors.add(errMsg);
            fieldErrorMsgs.add(errMsg);
          }else{
            if(detail.getPartServiceDesc().length() > 80){
              errMsg = malMessage.getMessage("exceeds.length", "Part_Service_Desc - Detail line " + i,"80");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
          }
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Part_Service_Desc"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }
          
          // Rule for Credit Documents and/or Invoices
          //**Qty  - Required; Quantity in the following format 10.25 or 8.00
          if(MALUtilities.isEmpty(detail.getQty())){
            errMsg = malMessage.getMessage("required.field", "Qty - Detail line " + i);
            validationErrors.add(errMsg);
            fieldErrorMsgs.add(errMsg);
          }else{
            if(!MALUtilities.isValidDecimalWMinMaxPrecision(detail.getQty(), 0, 2)){
                errMsg = malMessage.getMessage("must.matchWith", "Qty - Detail line " + i, "0000.00");
                validationErrors.add(errMsg);
                fieldErrorMsgs.add(errMsg);
            }
            if(detail.getQty().length() > 15){
              errMsg = malMessage.getMessage("exceeds.length", "Qty - Detail line " + i, "15");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
          }
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Qty"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }
          
          //**Unit_Cost - Requiured; Cost Per Unit (X Quantity will give a line Sub-Total)
          if(MALUtilities.isEmpty(detail.getUnitCost())){
            errMsg = malMessage.getMessage("required.field", "Unit_Cost - Detail line " + i);
            validationErrors.add(errMsg);
            fieldErrorMsgs.add(errMsg);
          }else{
            if(!MALUtilities.isValidDecimalWMinMaxPrecision(detail.getUnitCost(), 0, 2)){
              errMsg = malMessage.getMessage("must.matchWith","Unit_Cost - Detail line " + i, "0000.00");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
            
            if (detail.getUnitCost().length() > 10){
              errMsg = malMessage.getMessage("exceeds.length", "Unit_Cost - Detail line " + i,"10");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
          }
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Unit_Cost"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }
              
          //**Excise_Tax - Optional (As Applicable) Excise Tax
          if(!MALUtilities.isEmpty(detail.getExciseTax())){
            if(!MALUtilities.isValidDecimalWMinMaxPrecision(detail.getExciseTax(), 0, 2)){
                errMsg = malMessage.getMessage("must.matchWith","Excise_Tax - Detail line " + i, "0000.00");
                validationErrors.add(errMsg);
                fieldErrorMsgs.add(errMsg);
            }
            
            if(detail.getExciseTax().length() > 10){
              errMsg = malMessage.getMessage("exceeds.length", "Excise_Tax - Detail line " + i,"10");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
          }
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Excise_Tax"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }

          //**Disc_Rebate_Amount - Optional (As Applicable) Rebate or Discount Amount
          if(!MALUtilities.isEmpty(detail.getDiscRebateAmt())){
            if(!MALUtilities.isValidDecimalWMinMaxPrecision(detail.getDiscRebateAmt(), 0, 2)){
                errMsg = malMessage.getMessage("must.matchWith","Disc_Rebate_Amount - Detail line " + i, "0000.00");
                validationErrors.add(errMsg);
                fieldErrorMsgs.add(errMsg);
            }

            if(detail.getDiscRebateAmt().length() > 10){
              errMsg = malMessage.getMessage("exceeds.length", "Disc_Rebate_Amount - Detail line " + i,"10");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
          }
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Disc_Rebate_Amount"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }
          
          // Total Cost
          if(!MALUtilities.isEmpty(detail.getTotalCost())){
            if(!MALUtilities.isValidDecimalWMinMaxPrecision(detail.getTotalCost(), 0, 2)){
                errMsg = malMessage.getMessage("must.matchWith","Total_Cost - Detail line " + i, "0000.00");
                validationErrors.add(errMsg);
                fieldErrorMsgs.add(errMsg);
            }
            
            if(detail.getTotalCost().length() > 10){
              errMsg = malMessage.getMessage("exceeds.length", "Total_Cost - Detail line " + i,"10");
              validationErrors.add(errMsg);
              fieldErrorMsgs.add(errMsg);
            }
          }
          if(fieldErrorMsgs.size() > 0){
            fieldErrors.put("Total_Cost"+i, fieldErrorMsgs);
            fieldErrorMsgs = new ArrayList<String>();
          }         
          
        }       
      }
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
