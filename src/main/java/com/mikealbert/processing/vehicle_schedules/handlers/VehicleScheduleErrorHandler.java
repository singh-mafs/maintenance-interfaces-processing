package com.mikealbert.processing.vehicle_schedules.handlers;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.dao.ErrorCodeDAO;
import com.mikealbert.data.entity.ErrorCode;
import com.mikealbert.data.entity.MaintSchedulesProcessed;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.FleetMasterService;
import com.mikealbert.service.MaintenanceScheduleService;
import com.mikealbert.service.VehicleScheduleService;

@Component("vehicleScheduleErrorHandler")
public class VehicleScheduleErrorHandler {
	
	@Resource  MaintenanceScheduleService maintScheduleService;
	@Resource  VehicleScheduleService vehicleScheduleService;
	@Resource  FleetMasterService fleetMasterService;
	@Resource  ErrorCodeDAO errorCodeDAO;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public void updateErrorCode(MaintSchedulesProcessed req, @Headers Map<String, Object> headers) throws MalBusinessException{	
		//also update the processing table
		MaintSchedulesProcessed processRecord = maintScheduleService.findProcessRecordByFmsId(req.getFleetMaster().getFmsId());
		ErrorCode error = errorCodeDAO.findOne(Long.parseLong(headers.get("recordError").toString()));
		processRecord.setErrorCode(error);
		maintScheduleService.saveScheduleProcessRecord(processRecord);

		logger.info("Vehicle Schedule Assignment for FmsId - " + req.getFleetMaster().getFmsId() + " resulted in error code  - " + headers.get("recordError"));
	}
}
