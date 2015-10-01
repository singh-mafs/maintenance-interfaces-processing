package com.mikealbert.batch.mappers.mappinghelpers;

import java.math.BigDecimal;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.util.MALUtilities;

public class AddNonZeroDetailMappingHelper implements MappingHelper<Object> {

	@Override
	public void invokeHelperMethod(Object line, Object record) {
		BigDecimal 				zero 				= new BigDecimal("0.00");
		VendorInvoiceHeaderVO 	vendorInvoiceHeader	= (VendorInvoiceHeaderVO) record;
		VendorInvoiceDetailsVO 	vendorInvoiceDetail	= (VendorInvoiceDetailsVO) line;
		String					taxAmount			= vendorInvoiceDetail.getTaxAmount();
		String 					discRebateAmt		= vendorInvoiceDetail.getDiscRebateAmt();
		String					totalCost			= vendorInvoiceDetail.getTotalCost();
		String					unitCost			= vendorInvoiceDetail.getUnitCost();
		String					exciseTax			= vendorInvoiceDetail.getExciseTax();
		String					qty					= vendorInvoiceDetail.getQty();
		
		if(	   !MALUtilities.isEmpty(vendorInvoiceDetail.getPartServiceCode())
			|| !MALUtilities.isEmpty(vendorInvoiceDetail.getPartServiceDesc())
			|| (!MALUtilities.stringMatchesBigDecimal(taxAmount,zero)		&& !MALUtilities.isEmpty(taxAmount))
			|| (!MALUtilities.stringMatchesBigDecimal(discRebateAmt,zero)	&& !MALUtilities.isEmpty(discRebateAmt))
			|| (!MALUtilities.stringMatchesBigDecimal(totalCost,zero)		&& !MALUtilities.isEmpty(totalCost))
			|| (!MALUtilities.stringMatchesBigDecimal(unitCost,zero)		&& !MALUtilities.isEmpty(unitCost))
			|| (!MALUtilities.stringMatchesBigDecimal(exciseTax,zero)		&& !MALUtilities.isEmpty(exciseTax))
			|| (!MALUtilities.stringMatchesBigDecimal(qty,zero)				&& !MALUtilities.isEmpty(qty))) {
			vendorInvoiceHeader.addDetail(vendorInvoiceDetail);
		}
	}
}
