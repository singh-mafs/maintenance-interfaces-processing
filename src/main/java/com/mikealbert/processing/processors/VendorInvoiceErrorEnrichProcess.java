package com.mikealbert.processing.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.vo.VendorInvoiceErrorsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.exception.MalBusinessException;

@Component("vendorInvoiceErrorEnrichProcess")
public class VendorInvoiceErrorEnrichProcess {
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	@Handler
	public VendorInvoiceHeaderVO mapVendorInvoice(VendorInvoiceHeaderVO vendorInvoiceVo, @Headers Map<String, Object> headers) throws MalBusinessException{
		headers.put("loadId", vendorInvoiceVo.getLoadId());
		vendorInvoiceVo.setErrors(createErrorsList(headers));
		
		logger.info("Getting ready to write vendor invoice error for invoice : " + vendorInvoiceVo.getDocNumber());
		
		return vendorInvoiceVo;
	}	
	
	private List<VendorInvoiceErrorsVO> createErrorsList(@Headers Map<String, Object> headers){

		List<VendorInvoiceErrorsVO> invoiceErrors = new ArrayList<VendorInvoiceErrorsVO>();
		VendorInvoiceErrorsVO error;
		HashMap<String,List<String>> fieldErrors = (HashMap<String, List<String>>) headers.get("fieldErrors");
		for (String key : fieldErrors.keySet()) {
			List<String> errorDescs = fieldErrors.get(key);
			for(String errorDesc : errorDescs){
				error = new VendorInvoiceErrorsVO();
				error.setRecordType("E");
				error.setErrorFieldName(key);
				error.setErrorDesc(errorDesc);
				invoiceErrors.add(error);
			}
		}
		
		return invoiceErrors;
	}
}
