package com.mikealbert.batch.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.util.MALUtilities;


public class SearsDetailFieldSetMapper implements FieldSetMapper<VendorInvoiceDetailsVO> {
	@Value("${coupons.code}")
	private	String	couponCode;
	
	@Value("${coupons.codedesc}")
	private	String	couponDesc;
	
	@Override
	public VendorInvoiceDetailsVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceDetailsVO vendorInvoiceDetail = new VendorInvoiceDetailsVO();
		
		vendorInvoiceDetail.setRecordType("D");
		
		String totalCost = MALUtilities.parseWholeNumberToDecimal(fieldSet.readString("totalCost"), 2, 2);
		
		if (MALUtilities.isNumeric(totalCost) && !MALUtilities.isPositiveBigDecimal(totalCost)) {
			vendorInvoiceDetail.setPartServiceCode(couponCode);
			vendorInvoiceDetail.setPartServiceDesc(couponDesc);
			vendorInvoiceDetail.setQty("1");
			vendorInvoiceDetail.setUnitCost(totalCost);
			vendorInvoiceDetail.setTotalCost(totalCost);
			vendorInvoiceDetail.setDiscRebateAmt(totalCost);
		} else {
			String partServiceCode = fieldSet.readString("prePartServiceCode") + fieldSet.readString("partServiceCode");
			vendorInvoiceDetail.setPartServiceCode(partServiceCode);
			vendorInvoiceDetail.setPartServiceDesc(fieldSet.readString("partServiceDesc"));
			vendorInvoiceDetail.setQty(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("qty"), 0));
			vendorInvoiceDetail.setUnitCost(MALUtilities.parseWholeNumberToDecimal(fieldSet.readString("unitCost"), 2, 2));
			vendorInvoiceDetail.setTotalCost(totalCost);
		}
		
		return vendorInvoiceDetail;
	}
}