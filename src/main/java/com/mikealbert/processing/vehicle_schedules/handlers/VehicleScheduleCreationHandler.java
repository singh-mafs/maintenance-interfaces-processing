package com.mikealbert.processing.vehicle_schedules.handlers;

import javax.annotation.Resource;

import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

import com.mikealbert.data.entity.MaintSchedulesProcessed;
import com.mikealbert.data.entity.MasterSchedule;
import com.mikealbert.data.entity.VehicleSchedule;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.exception.MalDataValidationException;
import com.mikealbert.service.FleetMasterService;
import com.mikealbert.service.MaintenanceScheduleService;

@Component("vehicleScheduleCreateHandler")
public class VehicleScheduleCreationHandler {
	
	@Resource MaintenanceScheduleService maintenanceScheduleService;
	@Resource FleetMasterService fleetMasterService;
	
	
	@Handler
	public  VehicleSchedule create(MaintSchedulesProcessed req) throws MalBusinessException, MalDataValidationException{
		VehicleSchedule retVal = null;
		
		MasterSchedule masterSchedule = maintenanceScheduleService.determineMasterScheduleByFmsId(req.getFleetMaster().getFmsId()); 
		
		retVal = maintenanceScheduleService.createVehicleSchedule(masterSchedule, req.getFleetMaster());

		return retVal;
	}
}
