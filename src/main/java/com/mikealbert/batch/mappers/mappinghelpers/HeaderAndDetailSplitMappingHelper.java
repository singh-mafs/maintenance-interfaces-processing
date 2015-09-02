package com.mikealbert.batch.mappers.mappinghelpers;

import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;

public class HeaderAndDetailSplitMappingHelper implements MappingHelper<Object> {

	@Override
	public void invokeHelperMethod(Object line, Object record) {
		VendorInvoiceDetailsVO vendorInvoiceDetail = ((VendorInvoiceHeaderVO)line).getDetails().get(((VendorInvoiceHeaderVO)line).getDetails().size() -1);
		((VendorInvoiceHeaderVO)record).addDetail(vendorInvoiceDetail);
	}

}
