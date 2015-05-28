package com.mikealbert.batch.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.util.MALUtilities;


public class PepboysDetailFieldSetMapper implements FieldSetMapper<VendorInvoiceDetailsVO> {

	@Override
	public VendorInvoiceDetailsVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceDetailsVO vendorInvoiceDetail = new VendorInvoiceDetailsVO();
		
		// set the record type to (D)etail
		// Pepboys Detail records will always be denoted with our standard letter "D"
		vendorInvoiceDetail.setRecordType(fieldSet.readString("recordType"));
		vendorInvoiceDetail.setPartServiceDesc(fieldSet.readString("partServiceDesc"));
		vendorInvoiceDetail.setPartServiceCode(fieldSet.readString("partServiceCode"));
		vendorInvoiceDetail.setExciseTax(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("exciseTax"), 2));
		vendorInvoiceDetail.setDiscRebateAmt(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("discRebateAmt"), 2));
		vendorInvoiceDetail.setQty(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("qty"), 2));
		vendorInvoiceDetail.setUnitCost(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("unitCost"), 2));
		vendorInvoiceDetail.setTotalCost(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("totalCost"), 2));
		
		return vendorInvoiceDetail;
	}
}