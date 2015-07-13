package com.mikealbert.batch.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.mikealbert.common.MalConstants;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

public class BridgestoneFirestoneHeaderFieldSetMapper implements FieldSetMapper<VendorInvoiceHeaderVO> {
	
	@Override
	public VendorInvoiceHeaderVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceHeaderVO vendorInvoiceHeader = new VendorInvoiceHeaderVO();
		
		if(fieldSet.readString("recordType").equalsIgnoreCase("2")) {
			vendorInvoiceHeader.setRecordType("HDR");
			
			String plannedDate = "";
			if (!MALUtilities.isEmpty(fieldSet.readString("plannedDate"))) {
				plannedDate = fieldSet.readString("plannedDate").substring(0, 2)
						+ "/" + fieldSet.readString("plannedDate").substring(2, 4)
						+ "/" + MalConstants.DEFAULT_TWO_DIGIT_YEAR_PREFIX
						+ fieldSet.readString("plannedDate").substring(4);				
			}
			
			String docDate = "";
			if (!MALUtilities.isEmpty(fieldSet.readString("docDate"))) {
				docDate = fieldSet.readString("docDate").substring(0, 2)
						+ "/" + fieldSet.readString("docDate").substring(2, 4)
						+ "/" + MalConstants.DEFAULT_TWO_DIGIT_YEAR_PREFIX
						+ fieldSet.readString("docDate").substring(4);
			}			
			
			vendorInvoiceHeader.setStoreNbr(fieldSet.readString("storeNbr"));
			vendorInvoiceHeader.setDocType("INV");
			vendorInvoiceHeader.setPlannedDate(plannedDate);
			vendorInvoiceHeader.setDocDate(docDate);
			vendorInvoiceHeader.setDocNumber(fieldSet.readString("docNumber"));
			
		} else if (fieldSet.readString("recordType").equalsIgnoreCase("6")) {
			vendorInvoiceHeader.setRecordType("H");
			
			Integer requirementNumber = new Integer(0);
			if (MALUtilities.isNumber(fieldSet.readString("requirementNumber"))) {
				requirementNumber = new Integer(fieldSet.readString("requirementNumber"));
			}
			
			switch (requirementNumber) {
			case 33: // Unit Number
				vendorInvoiceHeader.setUnitNo(fieldSet.readString("requirementData"));
				break;
			case 469: // PO Number
				vendorInvoiceHeader.setPoNbr(fieldSet.readString("requirementData"));
				break;
			case 9006: // Driver Name
				vendorInvoiceHeader.setDriver(fieldSet.readString("requirementData"));
				break;
			case 9007: // Mileage
				vendorInvoiceHeader.setMileage(fieldSet.readString("requirementData"));
				break;
			case 600: // VIN
			case 9008: // VIN
				if(MALUtilities.isEmpty(vendorInvoiceHeader.getVin()) || requirementNumber.compareTo(600) == 0) {
					vendorInvoiceHeader.setVin(fieldSet.readString("requirementData"));
				}
				break;
			case 9014: // License Plate Number
				vendorInvoiceHeader.setPlateNo(fieldSet.readString("requirementData"));
				break;				
			default:
			}
		}
		
		return vendorInvoiceHeader;
	}
}
