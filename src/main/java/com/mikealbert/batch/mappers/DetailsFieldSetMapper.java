package com.mikealbert.batch.mappers;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.dao.ServiceProviderMaintenanceCodeDAO;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.MaintenanceCodeService;
import com.mikealbert.service.ServiceProviderService;
import com.mikealbert.util.MALUtilities;

/**
* This can be used with Valvoline only until it is fixed to become truly generic
* @author Mohan
*
*/
public class DetailsFieldSetMapper implements
		FieldSetMapper<VendorInvoiceDetailsVO> {
		
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());
	
	private Long parentProviderId;
	
	@Value("#{jobParameters[parentProviderId]}")
	public void setParentProviderId(Long parentProviderId) {
		this.parentProviderId = parentProviderId;
	}

	@Resource
	private	ServiceProviderMaintenanceCodeDAO	serviceProviderMaintenanceCodeDAO;
		
	
	@Resource
	private MaintenanceCodeService maintCodeService;
	@Resource
	private ServiceProviderService serviceProviderService;
	
	public   BigDecimal getRoundedValue(BigDecimal input, int scale){
    	if(input != null){
    		   return input.setScale(scale, BigDecimal.ROUND_HALF_UP);
    	}else{
    		//return null;
    		return BigDecimal.ZERO.setScale(scale);
    	}
	}
	
	public VendorInvoiceDetailsVO mapFieldSet(FieldSet fieldSet) {
		ServiceProvider parentProvider = serviceProviderService.getServiceProvider(parentProviderId);
		
		VendorInvoiceDetailsVO itemDetail = new VendorInvoiceDetailsVO();
		itemDetail.setRecordType("D");
		Long lineId = !MALUtilities.isEmpty(fieldSet.readString("INVLINENUM")) ? Long
				.parseLong(fieldSet.readString("INVLINENUM")) : null;
		itemDetail.setLineId(lineId);
		if(!MALUtilities.isEmpty(fieldSet.readString("QTY"))){
			itemDetail.setQty(getRoundedValue(new BigDecimal(fieldSet.readString("QTY")),2).toString());
		}else{
			itemDetail.setQty("1.00");
		}
		
		itemDetail.setPartServiceDesc(fieldSet.readString("ITEMDESC"));
		// Discount Rebate is not applicable for Valvoline at a line level
		//itemDetail.setDiscRebateAmt(!MALUtilities.isEmpty(fieldSet.readString("INVDISCAMT"))?  getRoundedValue(new BigDecimal(fieldSet.readString("INVDISCAMT")),2).toString():"0.00");
		itemDetail.setUnitCost(!MALUtilities.isEmpty(fieldSet.readString("RTLPRICE"))? getRoundedValue(new BigDecimal(fieldSet.readString("RTLPRICE")),2).toString():null);
		itemDetail.setTotalCost(!MALUtilities.isEmpty(fieldSet.readString("LINEAMT"))? getRoundedValue(new BigDecimal(fieldSet.readString("LINEAMT")),2).toString():null);
		
		
		//TODO: this is alot to put in what should be a simple field mapper class
		// this should be moved into a separate simpler component and wired this in (which also allows for reuse)
		// the original idea was to build a simple component we could use any time a vendor sends only a description
		if(parentProvider != null){
			List<ServiceProviderMaintenanceCode> maintCodes = maintCodeService.getServiceProviderCodeByDescription(
					fieldSet.readString("ITEMDESC"),this.parentProviderId);
						
			if(!MALUtilities.isEmpty(maintCodes) && maintCodes.size() > 0){
				itemDetail.setPartServiceCode(maintCodes.get(0).getCode());
			}else{
				// flagged the repair for review
				// save the maintenance code in supp_maint_code table
				ServiceProviderMaintenanceCode vendorCode = new ServiceProviderMaintenanceCode();		
				vendorCode.setServiceProvider(parentProvider);
				vendorCode.setDescription(itemDetail.getPartServiceDesc());
				try {
					maintCodeService.saveServiceProviderMaintCode(vendorCode);
				} catch (MalBusinessException e) {
					logger.error(e, "Error occured while auto-adding a valvoline Vendor Maint Code while processing Invoices");
				}
			}

		}
		
		return itemDetail;
	}
}
