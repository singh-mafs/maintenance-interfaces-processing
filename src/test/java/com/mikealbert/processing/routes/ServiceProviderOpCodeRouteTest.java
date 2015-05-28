package com.mikealbert.processing.routes;

import java.util.Properties;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;

import com.mikealbert.data.vo.StoreLocationVO;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ServiceProviderOpCodeRouteTest {

    @Autowired
    protected CamelContext context;
       
    @Produce(uri = "jms:queue:queue.vendorLocations")
    protected ProducerTemplate vendorLocationsProducer;
    
    @EndpointInject(uri = "mock:bean:serviceProviderSaveHandler")
    protected MockEndpoint end;
    
	@Test
	public void testRouteToSaveProducer() throws Exception {
		StoreLocationVO store = new StoreLocationVO();
		store.setStoreCode("TC1");
		store.setStoreName("Cassidy Tire & Service - TEST");
		store.setAddressLine1("1234 Somewhere Street");
		store.setCity("BROOKVILLE");
		store.setStateProv("OH");
		store.setZipCode("45309");
		store.setOperationCode("A");
		Properties props = new Properties();
		props.put("parentProviderId", 18655L);
		
		vendorLocationsProducer.sendBodyAndHeader(store, "parentProviderId", 18655L);
		//TODO: do some mock and assertions other than this
		MockEndpoint.assertIsSatisfied(end);
	}
	
	@Test
	public void testRouteToUpdateProducer() throws Exception {
		StoreLocationVO store = new StoreLocationVO();
		store.setStoreCode("CAL01");
		store.setStoreName("Cassidy Tire & Service - TEST");
		store.setAddressLine1("1234 Somewhere Street");
		store.setCity("BROOKVILLE");
		store.setStateProv("OH");
		store.setZipCode("45309");
		store.setOperationCode("M");
		Properties props = new Properties();
		props.put("parentProviderId", 19396L);
		
		vendorLocationsProducer.sendBodyAndHeader(store, "parentProviderId", 18655L);
		//TODO: do some mock and assertions other than this
		MockEndpoint.assertIsSatisfied(end);
	}
	
	@Test
	public void testRouteToDeleteProducer() throws Exception {
		StoreLocationVO store = new StoreLocationVO();
		store.setStoreCode("CAL01");
		store.setStoreName("Cassidy Tire & Service - TEST");
		store.setAddressLine1("1234 Somewhere Street");
		store.setCity("BROOKVILLE");
		store.setStateProv("OH");
		store.setZipCode("45309");
		store.setOperationCode("D");
		Properties props = new Properties();
		props.put("parentProviderId", 19396L);
		
		vendorLocationsProducer.sendBodyAndHeader(store, "parentProviderId", 18655L);
		//TODO: do some mock and assertions other than this
		MockEndpoint.assertIsSatisfied(end);
	}
}
