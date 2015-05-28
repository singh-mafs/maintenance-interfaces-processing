package com.mikealbert.processing.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;
import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceErrorsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.util.MALUtilities;

@Component("vendorInvoiceErrorEnrichProcess")
public class VendorInvoiceErrorEnrichProcess {

	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	@Handler
	public VendorInvoiceHeaderVO mapVendorInvoice(VendorInvoiceHeaderVO vendorInvoiceVo, @Headers Map<String, Object> headers) throws MalBusinessException{
		ServiceProvider provider = storeToProviderMappingHelper.getParentFromProperties(headers);
		addParentProviderHeader(headers,provider);
		addLoadIdHeader(headers,vendorInvoiceVo);
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
	
	private void addParentProviderHeader(@Headers Map<String, Object> headers, ServiceProvider provider){
		String parentFolderName = provider.getServiceProviderName().replaceAll("[^a-zA-Z0-9 \\.\\-]", "_").trim();
		headers.put("parentName", parentFolderName);
	}
	
	private void addLoadIdHeader(@Headers Map<String, Object> headers, VendorInvoiceHeaderVO vendorInvoiceVo){
		headers.put("loadId", vendorInvoiceVo.getLoadId());
	}
	
}
