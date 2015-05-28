package com.mikealbert.processing.routes;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.apache.camel.CamelContext;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:camelContextTest.xml" , "classpath:applicationContextTest.xml" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MaintenanceRequestStatusRouteTest   {

    @Autowired
    protected CamelContext camelContext;
            
    @Produce(uri = "direct:maintenanceRequestStatusPolling")
    protected ProducerTemplate fromPollingTemplate;
	
	@Ignore
	public void testRouteToMaintenanceRequestAutoStatusQueue() throws Exception {
		fromPollingTemplate.sendBody("");
		
		MockEndpoint.assertIsSatisfied(camelContext);
		
	}
	
	@Test
	public void testRouteFromAutoStatusQueueToSave() throws Exception {
		// TODO: placeholder
		assertTrue(true);
	}
}
