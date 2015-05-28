package com.mikealbert.processing.processors;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.data.vo.VendorMaintCodeVO;
import com.mikealbert.testing.BaseTest;

import static org.junit.Assert.*;


public class VendorMaintCodeAddMappingProcessTest extends BaseTest {

	@Resource VendorMaintCodeAddMappingProcess vendorMaintCodeAddMappingProcess;
	
	@Test
	public void testMapStoreLocation() throws Exception {
		VendorMaintCodeVO vendorMaintCode = constructVendorMaintCodeLocationVO();
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("parentProviderId", 19396L);
		
		ServiceProviderMaintenanceCode code = vendorMaintCodeAddMappingProcess.mapVendorMaintCode(vendorMaintCode, headers);
		
		assertEquals("02-001",code.getCode());
		assertEquals("19396",code.getServiceProvider().getServiceProviderId().toString());
		
	}
	
	private VendorMaintCodeVO constructVendorMaintCodeLocationVO(){
		VendorMaintCodeVO vo = new VendorMaintCodeVO();
		vo.setPartServiceCode("02-001");
		vo.setPartServiceDesc("particle filter, diesel");
		vo.setOperationCode("A");
		
		return vo;
	}
}
