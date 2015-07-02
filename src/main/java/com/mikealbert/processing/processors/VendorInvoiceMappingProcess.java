package com.mikealbert.processing.processors;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderInvoiceDetail;
import com.mikealbert.data.entity.ServiceProviderInvoiceDetailPK;
import com.mikealbert.data.entity.ServiceProviderInvoiceHeader;
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.util.MALUtilities;

@Component("vendorInvoiceMappingProcess")
public class VendorInvoiceMappingProcess {

	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	@Value("${coupons.code}")
	private	String	COUPONS_CODE;
	
	@Handler
	public ServiceProviderInvoiceHeader mapVendorInvoice(VendorInvoiceHeaderVO vendorInvoiceVo, @Headers Map<String, Object> headers) throws MalBusinessException{
		
		ServiceProviderInvoiceHeader vendorInvoice = new ServiceProviderInvoiceHeader();
		ServiceProvider provider = storeToProviderMappingHelper.getParentFromProperties(headers);
		addParentProviderHeader(headers,provider);
		addLoadIdHeader(headers,vendorInvoiceVo);
		
		vendorInvoice.setActionType("P"); // Process this record
		//TODO: this has no meaning for file export; it will need to be looked up from the 
		// account in we direct import into the DB
		//vendorInvoice.setApInd("Y");
		//vendorInvoice.setArInd("Y");
		
		if (MALUtilities.isEmpty(vendorInvoiceVo.getDocDate())) {
			vendorInvoice.setDocDate(new Date());
		} else {
			vendorInvoice.setDocDate(parseDate(vendorInvoiceVo.getDocDate()));
		}
		
		if (MALUtilities.isEmpty(vendorInvoiceVo.getDocNumber())) {
			vendorInvoice.setDocNo(vendorInvoiceVo.getDocNumber());
		} else{
			vendorInvoice.setDocNo(vendorInvoiceVo.getDocNumber().toUpperCase());
		}
		
		if (MALUtilities.isEmpty(vendorInvoiceVo.getPoNbr())) {
			vendorInvoice.setJobNo(vendorInvoiceVo.getPoNbr());
		} else{
			vendorInvoice.setJobNo(vendorInvoiceVo.getPoNbr().toUpperCase());
		}
		
		if (MALUtilities.isEmpty(vendorInvoiceVo.getVin())) {
			vendorInvoice.setVin(vendorInvoiceVo.getVin());
		} else {
			vendorInvoice.setVin(vendorInvoiceVo.getVin().toUpperCase());
		}
		
		vendorInvoice.setDocType(vendorInvoiceVo.getDocType());
		vendorInvoice.setDriver(vendorInvoiceVo.getDriver());
		vendorInvoice.setLineCount(BigDecimal.valueOf(vendorInvoiceVo.getDetails().size()).longValue());
		
		//TODO: this is a error!
		vendorInvoice.setLoadId(new Long(vendorInvoiceVo.getLoadId()));
		vendorInvoice.setLoadDate(new Date());
		
		vendorInvoice.setMileage(Long.parseLong(vendorInvoiceVo.getMileage()));
		vendorInvoice.setOrigDocNo(vendorInvoiceVo.getOrigDocNo());
		vendorInvoice.setParentProviderNumber(provider.getServiceProviderNumber());
		 
		vendorInvoice.setPlannedStart(parseDate(vendorInvoiceVo.getPlannedDate()));
		vendorInvoice.setRecordId(new Long(vendorInvoiceVo.getRecordId()));
		vendorInvoice.setRecordType(vendorInvoiceVo.getRecordType());
		vendorInvoice.setRegNo(vendorInvoiceVo.getPlateNo());
		vendorInvoice.setServiceProviderNumber(vendorInvoiceVo.getStoreNbr());
		vendorInvoice.setValidateInd("Y");
		vendorInvoice.setVendorRef(vendorInvoiceVo.getVendorRef());
		
		//Pad unit numbers with leading zeros
		if(MALUtilities.isNotEmptyString(vendorInvoiceVo.getUnitNo()) && MALUtilities.isNumber(vendorInvoiceVo.getUnitNo()) && vendorInvoiceVo.getUnitNo().length()>5){
			vendorInvoice.setUnitNo(String.format("%1$#" + 8 + "s", vendorInvoiceVo.getUnitNo()).replace(" ","0"));
		}else
		{
			vendorInvoice.setUnitNo(vendorInvoiceVo.getUnitNo());
		}
		vendorInvoice.setDetails(convertDetailsVoToInvoiceDetails(vendorInvoiceVo.getDetails(),vendorInvoice));
		
		logger.info("Getting ready to add vendor invoice : " + vendorInvoice.getJobNo());
		
		return vendorInvoice;
	}	
	
	private List<ServiceProviderInvoiceDetail> convertDetailsVoToInvoiceDetails(List<VendorInvoiceDetailsVO> detailsVos, ServiceProviderInvoiceHeader vendorInvoice) throws MalBusinessException{
		boolean isCreditDoc = isCreditDoc(vendorInvoice);
		boolean isInvoiceLevelDiscount = false;
		boolean isDiscountLine = false;
		boolean isCouponLine = false;
		
		List<ServiceProviderInvoiceDetail> invoiceDetails = new ArrayList<ServiceProviderInvoiceDetail>();
		for(VendorInvoiceDetailsVO detailVo : detailsVos){
			BigDecimal unitTotal = new BigDecimal(0.00);
			
			ServiceProviderInvoiceDetail detail = new ServiceProviderInvoiceDetail();
			ServiceProviderInvoiceDetailPK id = new ServiceProviderInvoiceDetailPK();
			id.setLineId(detailVo.getLineId());
			id.setRecordId(vendorInvoice.getRecordId());
			detail.setId(id);
			detail.setHeader(vendorInvoice);

			//ExcessQty is never used in the system today!
			// (always empty or 0)
			//detail.setExcessQty(detailVo.ge)
			
			//MrtId and SecondMrtId are not used it the import process
			//detail.setMrtId(mrtId)
			//detail.setSecondMrtId(secondMrtId)

			// Tax Lines are handled using special rules; it they send a line with Tax 
			// we treat it as Total Tax and update the Code and Description
			// to values that have special meaning in the system
			// we are only supposed to get 1 tax file per invoice
			if(MALUtilities.isNotEmptyString(detailVo.getTaxAmount()) && isValidDecimalWPercision(detailVo.getTaxAmount(),2) && (Double.parseDouble(detailVo.getTaxAmount()) != 0)){
				detail.setTaxAmount(setDecimalValueOnDocument(new BigDecimal(detailVo.getTaxAmount()).setScale(2, BigDecimal.ROUND_HALF_UP),isCreditDoc,isDiscountLine,isCouponLine));
				detail.setVendorCode("TX");
				detail.setDescription("SALES TAX");
				detail.setQuantity(new BigDecimal(1));
				detail.setUnitCost(new BigDecimal(0));
				detail.setTotalCost(detail.getTaxAmount().setScale(2, BigDecimal.ROUND_HALF_UP));	
			// Handle Invoice Level discounts / rebates
			}else if(MALUtilities.isNotEmptyString(detailVo.getDiscRebateAmt()) && isValidDecimalWPercision(detailVo.getDiscRebateAmt(),2) && (Double.parseDouble(detailVo.getDiscRebateAmt()) != 0) && MALUtilities.isEmptyString(detailVo.getPartServiceCode())){
				isDiscountLine = true;
				
				//D,154010202,,1,0,0,0,0,13.79,D,INVOICE DISCOUNT / REBATE
				isInvoiceLevelDiscount = true;
				detail.setDescription("INVOICE DISCOUNT / REBATE");
				detail.setLineType("D");
				detail.setQuantity(new BigDecimal(1));
				detail.setUnitCost(new BigDecimal(0));
				detail.setTaxAmount(new BigDecimal(0));
				detail.setExciseTax(new BigDecimal(0));
				detail.setTotalCost(new BigDecimal(0));	
				detail.setDiscRbAmt(setDecimalValueOnDocument(new BigDecimal(detailVo.getDiscRebateAmt()).setScale(2, BigDecimal.ROUND_HALF_UP), isCreditDoc, isDiscountLine,isCouponLine));					
			}else{
				detail.setVendorCode(detailVo.getPartServiceCode());	
				detail.setDescription(detailVo.getPartServiceDesc());
			
				if(MALUtilities.isNotEmptyString(detailVo.getDiscRebateAmt()) && isValidDecimalWPercision(detailVo.getDiscRebateAmt(),2) && (Double.parseDouble(detailVo.getDiscRebateAmt()) != 0)){
					// exclude COUPONS from flip
					if(!COUPONS_CODE.equals(detailVo.getPartServiceCode())){
						isDiscountLine = true;
						isCouponLine = false;
					}else{
						isDiscountLine = false;
						isCouponLine = true; 
					}
					detail.setDiscRbAmt(setDecimalValueOnDocument(new BigDecimal(detailVo.getDiscRebateAmt()).setScale(2, BigDecimal.ROUND_HALF_UP), isCreditDoc, isDiscountLine,isCouponLine));
				}else{
					isDiscountLine = false;
					detail.setDiscRbAmt(new BigDecimal(0.00));
				}
				
				if(MALUtilities.isNotEmptyString(detailVo.getExciseTax())){
					detail.setExciseTax(setDecimalValueOnDocument(new BigDecimal(detailVo.getExciseTax()).setScale(2, BigDecimal.ROUND_HALF_UP), isCreditDoc, isDiscountLine,isCouponLine));
				}else{
					detail.setExciseTax(new BigDecimal(0.00));
				}
				
				// if the value is negative (a credit) flip it to positive
				detail.setQuantity(setDecimalValueOnDocument(new BigDecimal(detailVo.getQty()).setScale(2, BigDecimal.ROUND_HALF_UP),isCreditDoc, isDiscountLine,isCouponLine));
				// if the value is negative (a credit) flip it to positive
				detail.setUnitCost(setDecimalValueOnDocument(new BigDecimal(detailVo.getUnitCost()).setScale(2, BigDecimal.ROUND_HALF_UP),isCreditDoc, isDiscountLine,isCouponLine));
				unitTotal = unitTotal.add(detail.getUnitCost());
				unitTotal = unitTotal.multiply(detail.getQuantity());
				
				//if the vendor supplied a total
				if(MALUtilities.isNotEmptyString(detailVo.getTotalCost())){
					//if the vendor supplied total cost does not match
					BigDecimal vendorTotalCost = new BigDecimal(detailVo.getTotalCost()).setScale(2, BigDecimal.ROUND_HALF_UP);
					
					if(!(unitTotal.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(vendorTotalCost) == 0)){
						//then set quanty to "1" and set unit cost and total cost
						//to total cost
						detail.setQuantity(new BigDecimal(1.0));
						detail.setUnitCost(vendorTotalCost);
						detail.setTotalCost(vendorTotalCost);
						
					}else{ //otherwise just set total cost
						detail.setTotalCost(unitTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}else{//otherwise just set the total cost
					detail.setTotalCost(unitTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
				}
			}
			invoiceDetails.add(detail);
		}
		
		return invoiceDetails;
	}
	
	private void addParentProviderHeader(@Headers Map<String, Object> headers, ServiceProvider provider){
		String parentFolderName = provider.getServiceProviderName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_").trim();
		headers.put("parentName", parentFolderName);
	}
	
	private void addLoadIdHeader(@Headers Map<String, Object> headers, VendorInvoiceHeaderVO vendorInvoiceVo){
		headers.put("loadId", vendorInvoiceVo.getLoadId());
	}
	
	//TODO: this is duplicated, if we have time move it to a common place
	private Date parseDate(String dateString) throws MalBusinessException{
		Date retVal = null;

		try {
			retVal = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			throw new MalBusinessException("Incorrect Date format : " + dateString);
		}
		
		return retVal;
	}

	//TODO: this is duplicated, if we have time move it to a common place
	private boolean isValidDecimalWPercision(String input, int precision) {
	     try {
	    	 double x = Double.parseDouble(input);
   		 String[] inputParts = input.split("\\.");
   		 if((inputParts.length > 1) && inputParts[1].length() != precision){
   			 return false;
   		 }else{
   			 return true;
   		 }
	     }
	     catch(NumberFormatException e){
	          return false;
	     }
	}
	
	//set document qty and price values
	private BigDecimal setDecimalValueOnDocument(BigDecimal input,boolean isCreditDoc,boolean isDiscountLine,boolean isCouponLine) throws MalBusinessException{
		
		if(isCreditDoc || isDiscountLine){
			if(input.doubleValue() < 0){
				return flipNegativeDecimalToPositive(input);
			}else{
				return input;
			}
		}else{
			if(isCouponLine){
				return input;
			}
			if(input.doubleValue() < 0){
				throw new MalBusinessException("Invoice Prices and Quanties cannot be less then 0.00  ");
			}else{
				return input;
			}
		}
	}
	
	//TODO: this could be common code
	private BigDecimal flipNegativeDecimalToPositive(BigDecimal input){
		BigDecimal retVal;
		if(input.doubleValue() < 0){
			retVal =  input.abs();
		}else{
			retVal = input;
		}
		
		return retVal;
		
	}
	
	private boolean isCreditDoc(ServiceProviderInvoiceHeader vendorInvoice){
		if(vendorInvoice.getDocType().equalsIgnoreCase("CRD")){
			return true;
		}else{
			return false;
		}
	}
}
