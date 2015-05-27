package com.mikealbert.processing.vehicle_schedules.handlers;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.entity.FleetMaster;
import com.mikealbert.data.entity.MaintSchedulesProcessed;
import com.mikealbert.data.entity.VehicleSchedule;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.exception.MalDataValidationException;
import com.mikealbert.exception.MalException;
import com.mikealbert.service.FleetMasterService;
import com.mikealbert.service.MaintenanceScheduleService;
import com.mikealbert.service.VehicleScheduleService;

@Component("vehicleScheduleSaveHandler")
public class VehicleScheduleSaveHandler {
	
	@Resource  MaintenanceScheduleService maintScheduleService;
	@Resource  VehicleScheduleService vehicleScheduleService;
	@Resource  FleetMasterService fleetMasterService;
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	@Handler
	public void save(VehicleSchedule req) throws MalBusinessException, MalException, MalDataValidationException{
		logger.info("Vehicle Schedule for FmsId - " + req.getFleetMaster().getFmsId() + " is being added");

		// lookup the FleetMaster from the DB because we are getting a transient object exception
		// due to serialization
		FleetMaster fleetMaster = fleetMasterService.getFleetMasterByFmsId(req.getFleetMaster().getFmsId());
		req.setFleetMaster(fleetMaster);
		vehicleScheduleService.saveVehicleSchedule(req);
		
		//also update the processing table
		MaintSchedulesProcessed processRecord = maintScheduleService.findProcessRecordByFmsId(req.getFleetMaster().getFmsId());
		processRecord.setVehicleSchedule(req);
		maintScheduleService.saveScheduleProcessRecord(processRecord);

		logger.info("Vehicle Schedule for FmsId - " + req.getFleetMaster().getFmsId() + " was added");
	}
}
