package com.mikealbert.batch.writers;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemWriter;

import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.processing.handlers.VendorMaintCodeSaveHandler;

@SuppressWarnings("hiding")
public class SupplierMaintCodeWriter<ServiceProviderMaintenanceCode> implements ItemWriter<ServiceProviderMaintenanceCode>  {

	@Resource VendorMaintCodeSaveHandler vendorMaintCodeSaveHandler;
	
	@Override
	public void write(List<? extends ServiceProviderMaintenanceCode> items)
			throws Exception {
		for(ServiceProviderMaintenanceCode item : items){
			vendorMaintCodeSaveHandler.save((com.mikealbert.data.entity.ServiceProviderMaintenanceCode) item);
		}
			
	}

}
