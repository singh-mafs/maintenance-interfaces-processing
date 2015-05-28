package com.mikealbert.batch.mappers;


import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

public class JiffyLubeHeaderFieldSetMapper implements FieldSetMapper<VendorInvoiceHeaderVO> {
	
	@Override
	public VendorInvoiceHeaderVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceHeaderVO vendorInvoiceHeader = new VendorInvoiceHeaderVO();
		
		if(fieldSet.readString("recordType").equalsIgnoreCase("HDR")) {
			vendorInvoiceHeader.setRecordType("HDR");
			vendorInvoiceHeader.setStoreNbr(MALUtilities.parseWholeNumberToDecimal(fieldSet.readString("storeNbr"), 0,0));
			vendorInvoiceHeader.setDocType("INV");
			vendorInvoiceHeader.setDriver(fieldSet.readString("driver"));
			vendorInvoiceHeader.setPlannedDate(MALUtilities.convertDateToSlashFormat(fieldSet.readString("plannedDate"), "yyyyMMdd"));
			vendorInvoiceHeader.setDocDate(MALUtilities.convertDateToSlashFormat(fieldSet.readString("plannedDate"), "yyyyMMdd"));
			vendorInvoiceHeader.setMileage(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("mileage"),0));
			vendorInvoiceHeader.setDocNumber(fieldSet.readString("docNumber"));
			
		} else if (fieldSet.readString("recordType").equalsIgnoreCase("FLT")) {
			vendorInvoiceHeader.setRecordType("H");
			
			Integer ruleId = new Integer(fieldSet.readString("ruleId"));
			switch (ruleId) {
			case 1: // Vin Number
				vendorInvoiceHeader.setVin(fieldSet.readString("ruleValue"));
				break;
			case 4: // Unit Number
				vendorInvoiceHeader.setUnitNo(fieldSet.readString("ruleValue"));
				break;
			case 95: // PO Number
				vendorInvoiceHeader.setPoNbr(fieldSet.readString("ruleValue"));
				break;
			default:
			}
		}
		
		return vendorInvoiceHeader;
	}
}