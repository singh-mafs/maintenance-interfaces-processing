package com.mikealbert.processing.vehicle_schedules.routes;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

import com.mikealbert.exception.MalDataValidationException;
import com.mikealbert.processing.vehicle_schedules.handlers.MalDataValidationExceptionHandler;

public class VehicleMaintScheduleAssignRoute extends RouteBuilder {
	@Value("${maintScheduleAssignRoute.cron}") 
	private String maintScheduleAssignCron;
	
	@Value("${maintScheduleAssignRoute.consumerCnt}") 
	private String maintScheduleAssignConsumers;
	
	@Override
	public void configure() throws Exception {
		from("quartz://maintAssignTimer?cron=" + maintScheduleAssignCron)
		.to("bean:maintenanceScheduleService?method=getMaintSchedulesForVehSchedAssignment")
		.split(body())
		.to("jms:queue:queue.maintScheduleAssign");

		from("jms:queue:queue.maintScheduleAssign?concurrentConsumers=" + maintScheduleAssignConsumers)
		.onException(MalDataValidationException.class)
			.handled(true).setExchangePattern(ExchangePattern.InOnly).useOriginalMessage()
		.process(new MalDataValidationExceptionHandler())
		.to("bean:vehicleScheduleErrorHandler")
		.end()
		.transacted("PROPAGATION_REQUIRED")
		.to("bean:vehicleScheduleCreateHandler")
		.to("bean:vehicleScheduleSaveHandler");
	}
}
