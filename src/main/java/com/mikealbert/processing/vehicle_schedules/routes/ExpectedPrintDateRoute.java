package com.mikealbert.processing.vehicle_schedules.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

public class ExpectedPrintDateRoute extends RouteBuilder {
	
	@Value("${expectedPrintDateRoute.cron}") 
	private String expectedPrintDateCron;
	
	@Value("${queuedSchedulesProcessed.consumerCnt}") 
	private String queuedSchedulesProcessedConsumers;	
	
	@Override
	public void configure() throws Exception {
		
		from("quartz://expectedPrintDateTimer?cron=" + expectedPrintDateCron)
		.to("bean:maintenanceScheduleService?method=getMaintSchedulesForDetermineExpPrintDate")
		.split(body())
		.to("jms:queue:queue.schedulesProcessed");
		
		from("jms:queue:queue.schedulesProcessed?concurrentConsumers=" + queuedSchedulesProcessedConsumers)
		.to("bean:maintenanceScheduleService?method=determineExpectedPrintDate")
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:expectedPrintDateHandler");
	}
}
