package com.mikealbert.processing.vehicle_schedules.handlers;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.MaintSchedulesProcessed;
import com.mikealbert.exception.MalException;
import com.mikealbert.service.MaintenanceScheduleService;
import com.mikealbert.util.MALUtilities;


@Component("expectedPrintDateHandler")
public class ExpectedPrintDateHandler {
	
	@Resource  MaintenanceScheduleService maintenanceScheduleService;

	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());

	@Handler
	public void save(MaintSchedulesProcessed req, @Headers Map<String, Object> headers) throws MalException {
		MaintSchedulesProcessed processRecord = maintenanceScheduleService.findProcessRecordByFmsId(req.getFleetMaster().getFmsId());
		if ((processRecord.getExpectedPrintDate() == null && req.getExpectedPrintDate() != null) || (processRecord.getExpectedPrintDate() != null &&  req.getExpectedPrintDate() != null && MALUtilities.compateDates(processRecord.getExpectedPrintDate(), req.getExpectedPrintDate()) != 0)){
			processRecord.setExpectedPrintDate(req.getExpectedPrintDate());
			
			logger.info("Maintenance Schedules Processed record - " + req.getFleetMaster().getFmsId() + " expected print date " + req.getExpectedPrintDate() + " is being updated");
			
			maintenanceScheduleService.saveScheduleProcessRecord(processRecord);
		}
	}
}
