package com.mikealbert.batch.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceHeaderVO;

public class VendorInvoiceHeaderFieldSetMapper implements FieldSetMapper<VendorInvoiceHeaderVO> {
	
	@Override
	public VendorInvoiceHeaderVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceHeaderVO vendorInvoiceHeader = new VendorInvoiceHeaderVO();
		
		vendorInvoiceHeader.setRecordType(fieldSet.readString("recordType").trim());
		vendorInvoiceHeader.setUnitNo(fieldSet.readString("unitNo").trim());
		vendorInvoiceHeader.setVin(fieldSet.readString("vin").trim());
		vendorInvoiceHeader.setPoNbr(fieldSet.readString("poNbr").trim());
		vendorInvoiceHeader.setStoreNbr(fieldSet.readString("storeNbr").trim());
		vendorInvoiceHeader.setDocType(fieldSet.readString("docType").trim());
		vendorInvoiceHeader.setDocNumber(fieldSet.readString("docNumber").trim());
		vendorInvoiceHeader.setDriver(fieldSet.readString("driver").trim());
		vendorInvoiceHeader.setVendorRef(fieldSet.readString("vendorRef").trim());
		vendorInvoiceHeader.setPlannedDate(fieldSet.readString("plannedDate").trim());
		vendorInvoiceHeader.setMileage(fieldSet.readString("mileage").trim());
		vendorInvoiceHeader.setPlateNo(fieldSet.readString("plateNo").trim());
		vendorInvoiceHeader.setOrigDocNo(fieldSet.readString("origDocNo").trim());
		
		String docDate = "";
		try {
			docDate = fieldSet.readString("docDate").trim();
		} catch (Exception ex) {}
		vendorInvoiceHeader.setDocDate(docDate);
		
		return vendorInvoiceHeader;
	}
}
