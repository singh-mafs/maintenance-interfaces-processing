package com.mikealbert.processing.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderInvoiceDetail;
import com.mikealbert.data.entity.ServiceProviderInvoiceHeader;
import com.mikealbert.data.util.MaintCodeMappingHelper;
import com.mikealbert.data.util.StoreToProviderMappingHelper;
import com.mikealbert.data.vo.VendorMaintCodeVO;
import com.mikealbert.service.MaintenanceCodeService;
import com.mikealbert.util.MALUtilities;

@Component("detectNewVendorMaintCodes")
public class DetectNewVendorMaintCodes {
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	@Resource StoreToProviderMappingHelper storeToProviderMappingHelper;
	@Resource MaintCodeMappingHelper maintCodeMappingHelper;
	@Resource MaintenanceCodeService maintCodeService;
	
	private static final String TX_CODE = "TX";

	@Handler
	public List<VendorMaintCodeVO> mapVendorMaintCode(ServiceProviderInvoiceHeader serviceProviderInvoice, @Headers Map<String, Object> headers){
		boolean isExists = true;
		List<VendorMaintCodeVO> newCodes = new ArrayList<VendorMaintCodeVO>();
		ServiceProvider parent = storeToProviderMappingHelper.getParentFromProperties(headers);
		for(ServiceProviderInvoiceDetail detail : serviceProviderInvoice.getDetails()){
			//if tax code is is filled in; then skip. also if vendor code is empty (which is the case for invoice discounts) also skip
			if(MALUtilities.isEmptyString(detail.getVendorCode()) || !detail.getVendorCode().equalsIgnoreCase(TX_CODE)){
				//see if the code currently exists; if it does not then create and return it
				isExists = maintCodeService.isServiceProviderCodeAdded(detail.getVendorCode(), parent.getServiceProviderId(), false);
				if(!isExists){
					// get the message id
					String messageId = (String) headers.get("jmsmessageid");
					// add a new VendorMaintCodeVO by converting the line in the invoice into a VO
					newCodes.add(convertProviderInvoiceDetailToVendorMaintCodeVO(detail,messageId,parent.getServiceProviderName()));
					
					logger.info("Vendor Code - " + detail.getVendorCode() + " was detected as a new code on Invoice " +  serviceProviderInvoice.getDocNo()  + " for Vendor : " + parent.getServiceProviderName());
				}
			}
		}
		//otherwise return nothing (null)
		return newCodes;
	}
	
	private VendorMaintCodeVO convertProviderInvoiceDetailToVendorMaintCodeVO(ServiceProviderInvoiceDetail detail, String messageId, String parentProvider){
		VendorMaintCodeVO vendorMaintCodeVO = new VendorMaintCodeVO();
		vendorMaintCodeVO.setMessageId(messageId);
		vendorMaintCodeVO.setParentProvider(parentProvider);
		vendorMaintCodeVO.setOperationCode("A");
		vendorMaintCodeVO.setPartServiceCode(detail.getVendorCode());
		vendorMaintCodeVO.setPartServiceDesc(detail.getDescription());
		return vendorMaintCodeVO;
	}
	
}
