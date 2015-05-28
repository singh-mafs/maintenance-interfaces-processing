package com.mikealbert.batch.mappers;

import java.math.BigDecimal;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

public class PepboysHeaderFieldSetMapper implements FieldSetMapper<VendorInvoiceHeaderVO> {

	@Override
	public VendorInvoiceHeaderVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceHeaderVO vendorInvoiceHeader = new VendorInvoiceHeaderVO();
		
		// set the record type to (H)eader
		// Pepboys Header records will always be denoted with our standard letter "H"
		vendorInvoiceHeader.setRecordType(fieldSet.readString("recordType"));
		// set the doc type to be (INV)OICE
		vendorInvoiceHeader.setDocType("INV");
		vendorInvoiceHeader.setDocDate(MALUtilities.convertDateToSlashFormat(fieldSet.readString("docDate"), "yyyyMMdd"));
		vendorInvoiceHeader.setPlannedDate(MALUtilities.convertDateToSlashFormat(fieldSet.readString("plannedDate"), "yyyyMMdd"));
		vendorInvoiceHeader.setMileage(fieldSet.readString("mileage"));
		vendorInvoiceHeader.setVin(fieldSet.readString("vin"));
		vendorInvoiceHeader.setUnitNo(fieldSet.readString("unitNo"));
		vendorInvoiceHeader.setDriver(fieldSet.readString("driver"));
		vendorInvoiceHeader.setDocNumber(fieldSet.readString("docNumber"));
		vendorInvoiceHeader.setPoNbr(fieldSet.readString("poNbr"));
		vendorInvoiceHeader.setStoreNbr(fieldSet.readString("storeNbr"));
		vendorInvoiceHeader.setPlateNo(fieldSet.readString("plateNo"));
		
		if(!MALUtilities.stringMatchesBigDecimal(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("salesTaxAmount"), 2), new BigDecimal("0.00"))){
			// setting up the tax line
			VendorInvoiceDetailsVO taxDetail = new VendorInvoiceDetailsVO();
			taxDetail.setRecordType("D");
			taxDetail.setQty("0.00");
			taxDetail.setUnitCost("0.00");
			taxDetail.setExciseTax("0.00");
			taxDetail.setDiscRebateAmt("0.00");
			taxDetail.setTaxAmount(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("salesTaxAmount"), 2));
			
			vendorInvoiceHeader.addDetail(taxDetail);			
		}
		
		return vendorInvoiceHeader;
	}
}