package com.mikealbert.batch.mappers.mappinghelpers;

import java.math.BigDecimal;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

public class TaxAndRebateSplitMappingHelper implements MappingHelper<Object> {

	@Override
	public void invokeHelperMethod(Object line, Object record) {
		BigDecimal zero = new BigDecimal("0.00");
		
		BigDecimal taxAmount = new BigDecimal(MALUtilities.parseDecimalNumberAndRound(((VendorInvoiceDetailsVO)line).getTaxAmount(), 2));
		if(taxAmount.compareTo(zero) > 0) {
			VendorInvoiceDetailsVO taxDetail = new VendorInvoiceDetailsVO();
			taxDetail.setRecordType("D");
			taxDetail.setQty("0.00");
			taxDetail.setUnitCost("0.00");
			taxDetail.setExciseTax("0.00");
			taxDetail.setTaxAmount(taxAmount.toString());
			((VendorInvoiceHeaderVO)record).addDetail(taxDetail);
		}
		
		BigDecimal rebateAmount = new BigDecimal(MALUtilities.parseDecimalNumberAndRound(((VendorInvoiceDetailsVO)line).getDiscRebateAmt(), 2));
		if(rebateAmount.compareTo(zero) > 0) {
			VendorInvoiceDetailsVO rebateDetail = new VendorInvoiceDetailsVO();
			rebateDetail.setRecordType("D");
			rebateDetail.setQty("0.00");
			rebateDetail.setUnitCost("0.00");
			rebateDetail.setExciseTax("0.00");		
			rebateDetail.setDiscRebateAmt(rebateAmount.toString());
			((VendorInvoiceHeaderVO)record).addDetail(rebateDetail);
		}
	}

}
