package com.mikealbert.processing.processors;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.testing.BaseTest;

import static org.junit.Assert.*;


public class StoreAddMappingProcessTest extends BaseTest {

	@Resource StoreAddMappingProcess storeAddMappingProcess;
	
	@Test
	public void testMapStoreLocation() throws Exception {
		StoreLocationVO loc = constructStoreLocationVO();
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("parentProviderId", 19396L);
		
		ServiceProvider provider = storeAddMappingProcess.mapStoreLocation(loc, headers);
		
		assertEquals("TEST CODE-2",provider.getServiceProviderNumber());
		assertEquals("19396",provider.getParentServiceProvider().getServiceProviderId().toString());
		
	}
	
	private StoreLocationVO constructStoreLocationVO(){
		StoreLocationVO vo = new StoreLocationVO();
		vo.setStoreCode("Test Code-2");
		vo.setStoreName("Test Vendor Name");
		vo.setTelephoneNumber("952-884-1999");
		vo.setAddressLine1("102-08 156th Road");
		vo.setCity("Howard Beach");
		vo.setStateProv("NY");
		vo.setZipCode("11414");
		vo.setOperationCode("A");
		
		return vo;
	}
}
