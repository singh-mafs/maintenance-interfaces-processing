package com.mikealbert.batch.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.util.MALUtilities;


public class JiffyLubeDetailFieldSetMapper implements FieldSetMapper<VendorInvoiceDetailsVO> {
	@Value("${coupons.code}")
	private	String	couponCode;
	
	@Value("${coupons.codedesc}")
	private	String	couponDesc;
	
	@Override
	public VendorInvoiceDetailsVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceDetailsVO vendorInvoiceDetail = new VendorInvoiceDetailsVO();
		
		if (fieldSet.readString("recordType").equalsIgnoreCase("DTL")) {
			vendorInvoiceDetail.setRecordType("D");
			
			String totalCost = MALUtilities.ebcdicToNumericAscii(fieldSet.readString("totalCost"), 2, 2);
			
			if ( MALUtilities.isNumeric(totalCost) && !MALUtilities.isPositiveBigDecimal(totalCost)) {
				vendorInvoiceDetail.setPartServiceCode(couponCode);
				vendorInvoiceDetail.setPartServiceDesc(couponDesc);
				vendorInvoiceDetail.setQty("1");
				vendorInvoiceDetail.setUnitCost(totalCost);
				vendorInvoiceDetail.setTotalCost(totalCost);
				vendorInvoiceDetail.setDiscRebateAmt(totalCost);
			} else {
				vendorInvoiceDetail.setPartServiceCode(fieldSet.readString("partServiceCode"));
				vendorInvoiceDetail.setPartServiceDesc(fieldSet.readString("partServiceDesc"));
				vendorInvoiceDetail.setQty(MALUtilities.ebcdicToNumericAscii(fieldSet.readString("qty"), 2, 2));
				vendorInvoiceDetail.setUnitCost(MALUtilities.ebcdicToNumericAscii(fieldSet.readString("unitCost"), 2, 2));
				vendorInvoiceDetail.setTotalCost(totalCost);
			}
		} else if (fieldSet.readString("recordType").equalsIgnoreCase("TND")) {
			vendorInvoiceDetail.setRecordType("TND");		
			vendorInvoiceDetail.setDiscRebateAmt(MALUtilities.ebcdicToNumericAscii(fieldSet.readString("discRebateAmt"), 2, 2));
			vendorInvoiceDetail.setTaxAmount(MALUtilities.ebcdicToNumericAscii(fieldSet.readString("taxAmount"), 2, 2));
		}	
		
		return vendorInvoiceDetail;
	}
}
