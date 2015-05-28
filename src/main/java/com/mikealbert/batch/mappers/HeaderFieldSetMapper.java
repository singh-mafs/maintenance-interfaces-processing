package com.mikealbert.batch.mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

/**
 * This can be used with Valvoline only until it is fixed to become truly generic
 * @author Mohan
 *
 */
public class HeaderFieldSetMapper implements FieldSetMapper<VendorInvoiceHeaderVO>{
	@SuppressWarnings("serial")
	private  final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
		{
			put("^\\d{8}$", "yyyyMMdd");
			put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
			put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
			put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
			put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");

		}
	};
	public VendorInvoiceHeaderVO mapFieldSet(FieldSet fieldSet) {
		VendorInvoiceHeaderVO itemHeader = new VendorInvoiceHeaderVO();
		itemHeader.setRecordType("H");
		itemHeader.setDocType("INV");
		//TODO: doc date should be today date
		itemHeader.setDocDate(getFormatedDate(fieldSet.readString("INVDT")));
		itemHeader.setPlannedDate(getFormatedDate(fieldSet.readString("INVDT")));
		itemHeader.setMileage(fieldSet.readString("CURRODOMETER"));
		itemHeader.setVin(fieldSet.readString("VIN"));
		itemHeader.setUnitNo(fieldSet.readString("UNIT"));
		itemHeader.setDriver(fieldSet.readString("CURRDRIVERNAME"));
		itemHeader.setDocNumber(fieldSet.readString("INVNUM"));
		itemHeader.setPoNbr(fieldSet.readString("PO"));
		itemHeader.setStoreNbr(fieldSet.readString("STORENUM"));
		itemHeader.setPlateNo(fieldSet.readString("LICENSEPLATENUM"));
		return itemHeader;
	}
	 public  String determineDateFormat(String dateString) {
	        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
	            if (dateString.toLowerCase().matches(regexp)) {
	                return DATE_FORMAT_REGEXPS.get(regexp);
	            }
	        }
	        return null; // Unknown format.
	    }
	 public String getFormatedDate(String dateString){
		 if(!MALUtilities.isEmpty(dateString)){
			 String inDateFormat = determineDateFormat(dateString);
			 if(inDateFormat == null){
				 return null;
			 }
			 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inDateFormat);
		     simpleDateFormat.setLenient(false); // Don't automatically convert invalid date.
		     try {
				Date date = simpleDateFormat.parse(dateString);
				String outDateFormat = "MM/dd/yyyy";
				simpleDateFormat	= new SimpleDateFormat(outDateFormat);
				return simpleDateFormat.format(date);
			} catch (ParseException e) {
				return null;
			}
		 }else{
			 return null;
		 }
	 }
}
