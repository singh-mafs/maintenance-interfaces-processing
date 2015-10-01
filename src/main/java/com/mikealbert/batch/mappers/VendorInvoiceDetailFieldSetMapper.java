package com.mikealbert.batch.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;


public class VendorInvoiceDetailFieldSetMapper implements FieldSetMapper<VendorInvoiceDetailsVO> {
	
	@Override
	public VendorInvoiceDetailsVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceDetailsVO vendorInvoiceDetail = new VendorInvoiceDetailsVO();
		
		vendorInvoiceDetail.setRecordType(fieldSet.readString("recordType").trim());
		vendorInvoiceDetail.setPartServiceCode(fieldSet.readString("partServiceCode").trim());
		vendorInvoiceDetail.setPartServiceDesc(fieldSet.readString("partServiceDesc").trim());
		vendorInvoiceDetail.setQty(fieldSet.readString("qty").trim());
		vendorInvoiceDetail.setUnitCost(fieldSet.readString("unitCost").trim());
		vendorInvoiceDetail.setTaxAmount(fieldSet.readString("taxAmount").trim());
		vendorInvoiceDetail.setExciseTax(fieldSet.readString("exciseTax").trim());
		vendorInvoiceDetail.setDiscRebateAmt(fieldSet.readString("discRebateAmt").trim());
		
		String totalCost = "";
		try {
			totalCost = fieldSet.readString("totalCost").trim();
		} catch (Exception ex) {}
		vendorInvoiceDetail.setTotalCost(totalCost);
		
		return vendorInvoiceDetail;
	}
}