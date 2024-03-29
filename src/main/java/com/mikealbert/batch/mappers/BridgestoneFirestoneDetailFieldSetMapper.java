package com.mikealbert.batch.mappers;

import java.math.BigDecimal;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.util.MALUtilities;


public class BridgestoneFirestoneDetailFieldSetMapper implements FieldSetMapper<VendorInvoiceDetailsVO> {
	@Value("${coupons.code}")
	private	String	couponCode;
	
	@Value("${coupons.codedesc}")
	private	String	couponDesc;
	
	@Override
	public VendorInvoiceDetailsVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceDetailsVO vendorInvoiceDetail = new VendorInvoiceDetailsVO();
		
		if (fieldSet.readString("recordType").equalsIgnoreCase("3")) {
			vendorInvoiceDetail.setRecordType("D");
			vendorInvoiceDetail.setPartServiceCode(fieldSet.readString("partServiceCode"));
			vendorInvoiceDetail.setPartServiceDesc(fieldSet.readString("partServiceDesc"));
			
			String totalCost = MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("totalCost"), 2);
			if (MALUtilities.isNumeric(totalCost) && !MALUtilities.isPositiveBigDecimal(totalCost)) {
				vendorInvoiceDetail.setPartServiceCode(couponCode);
				vendorInvoiceDetail.setPartServiceDesc(couponDesc);
				vendorInvoiceDetail.setQty("1");
				vendorInvoiceDetail.setUnitCost(totalCost);
				vendorInvoiceDetail.setTotalCost(totalCost);
				vendorInvoiceDetail.setDiscRebateAmt(totalCost);
				
				String exciseTax = MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("exciseTax"), 2);
				try{
					BigDecimal exciseTaxDecimal = new BigDecimal(exciseTax);
					//HD-147 We don't have to make a change for HD-147 here because we are hard coding quantity as 1.
					exciseTax = exciseTaxDecimal.abs().toString();
				} catch(NumberFormatException e) {}
				vendorInvoiceDetail.setExciseTax(exciseTax);
				
			} else {
				vendorInvoiceDetail.setQty(fieldSet.readString("qty"));
				vendorInvoiceDetail.setExciseTax(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("exciseTax"), 2));
				//Change made for HD147 Saket 04/18/2017. BridgeStone sends us excise tax for unit. we have to multiply it with quantity to get excise for line item
				if (!MALUtilities.isEmpty(vendorInvoiceDetail.getExciseTax())) {
					BigDecimal  vExciseTax   = new BigDecimal(vendorInvoiceDetail.getExciseTax());
					BigDecimal  vQty   = new BigDecimal(vendorInvoiceDetail.getQty());
					vExciseTax = vExciseTax.multiply(vQty);
					vendorInvoiceDetail.setExciseTax(vExciseTax.toString());
				}
				vendorInvoiceDetail.setUnitCost(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("unitCost"), 2));
				vendorInvoiceDetail.setTotalCost(totalCost);
			}
			
		} else if (fieldSet.readString("recordType").equalsIgnoreCase("9")) {
			vendorInvoiceDetail.setRecordType("TND");
			
			BigDecimal discRebateAmt = new BigDecimal("0.0");
			if(MALUtilities.isNumeric(fieldSet.readString("discRebateAmt"))) {
				discRebateAmt = new BigDecimal(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("discRebateAmt"), 2));
			}
			vendorInvoiceDetail.setDiscRebateAmt(discRebateAmt.abs().toString());
			
			String salesTax = MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("taxAmount"), 2);
			try{
				BigDecimal salesTaxDecimal = new BigDecimal(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("taxAmount"), 2));
				salesTax = salesTaxDecimal.abs().toString();
			} catch(NumberFormatException e) {}			
			vendorInvoiceDetail.setTaxAmount(salesTax);
		}
		
		return vendorInvoiceDetail;
	}
}