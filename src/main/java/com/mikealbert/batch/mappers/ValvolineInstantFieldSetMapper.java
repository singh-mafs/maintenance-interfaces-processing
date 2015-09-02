package com.mikealbert.batch.mappers;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;

import com.mikealbert.common.MalLogger;
import com.mikealbert.common.MalLoggerFactory;
import com.mikealbert.data.dao.ServiceProviderMaintenanceCodeDAO;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderMaintenanceCode;
import com.mikealbert.data.vo.VendorInvoiceDetailsVO;
import com.mikealbert.data.vo.VendorInvoiceHeaderVO;
import com.mikealbert.exception.MalBusinessException;
import com.mikealbert.service.MaintenanceCodeService;
import com.mikealbert.service.ServiceProviderService;
import com.mikealbert.util.MALUtilities;


public class ValvolineInstantFieldSetMapper implements FieldSetMapper<VendorInvoiceHeaderVO> {
	
	private MalLogger logger = MalLoggerFactory.getLogger(this.getClass());	
	
	@Value("${coupons.code}")
	private	String	couponCode;
	
	@Value("${coupons.codedesc}")
	private	String	couponDesc;
	
	private Long parentProviderId;

	@Resource
	private MaintenanceCodeService maintCodeService;

	@Resource
	private ServiceProviderService serviceProviderService;
	
	@Resource
	private	ServiceProviderMaintenanceCodeDAO	serviceProviderMaintenanceCodeDAO;		
	
	@Override
	public VendorInvoiceHeaderVO mapFieldSet(FieldSet fieldSet) throws BindException {
		VendorInvoiceHeaderVO vendorInvoiceHeader = new VendorInvoiceHeaderVO();
		VendorInvoiceDetailsVO vendorInvoiceDetail = null;
				
		vendorInvoiceHeader.setDocType("INV");
		vendorInvoiceHeader.setRecordType("H");
		vendorInvoiceHeader.setUnitNo(fieldSet.readString("unitNo"));
		vendorInvoiceHeader.setVin(fieldSet.readString("vin"));
		vendorInvoiceHeader.setPoNbr(fieldSet.readString("poNbr"));
		vendorInvoiceHeader.setStoreNbr(fieldSet.readString("storeNbr"));
		vendorInvoiceHeader.setDocNumber(fieldSet.readString("docNumber"));
		vendorInvoiceHeader.setDriver(fieldSet.readString("driver"));
		vendorInvoiceHeader.setPlannedDate(MALUtilities.convertDateFormat(fieldSet.readString("docDate"), "yyyy-MM-dd", "MM/dd/yyyy"));
		vendorInvoiceHeader.setDocDate(MALUtilities.convertDateFormat(fieldSet.readString("docDate"), "yyyy-MM-dd", "MM/dd/yyyy"));
		vendorInvoiceHeader.setMileage(fieldSet.readString("mileage"));
		vendorInvoiceHeader.setPlateNo(fieldSet.readString("plateNo"));
		
		// sales tax Detail line
		String taxAmount = MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("taxAmount"), 2);
		if (!MALUtilities.isEmpty(taxAmount) && !MALUtilities.stringMatchesBigDecimal(taxAmount, new BigDecimal ("0"))) {
			vendorInvoiceDetail = new VendorInvoiceDetailsVO();
			vendorInvoiceDetail.setRecordType("D");
			vendorInvoiceDetail.setTaxAmount(taxAmount);
			vendorInvoiceHeader.addDetail(vendorInvoiceDetail);
		}
		
		// Discount/Rebate Detail line
		String discountAmount = MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("discountAmount"), 2);
		if (!MALUtilities.isEmpty(discountAmount) && !MALUtilities.stringMatchesBigDecimal(discountAmount, new BigDecimal ("0"))) {
			vendorInvoiceDetail = new VendorInvoiceDetailsVO();
			vendorInvoiceDetail.setRecordType("D");
			vendorInvoiceDetail.setDiscRebateAmt(discountAmount);		
			vendorInvoiceHeader.addDetail(vendorInvoiceDetail);
		}
		
		// Coupon line
		String couponVal = MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("couponVal"), 2);
		if (!MALUtilities.isEmpty(fieldSet.readString("couponVal")) && !MALUtilities.stringMatchesBigDecimal(couponVal, new BigDecimal ("0"))) {
			vendorInvoiceDetail = new VendorInvoiceDetailsVO();
			couponVal = new BigDecimal(couponVal).negate().toString();
			
			vendorInvoiceDetail.setRecordType("D");
			vendorInvoiceDetail.setPartServiceCode(couponCode);
			vendorInvoiceDetail.setPartServiceDesc(couponDesc);
			vendorInvoiceDetail.setQty("1");
			vendorInvoiceDetail.setUnitCost(couponVal);
			vendorInvoiceDetail.setTotalCost(couponVal);
			vendorInvoiceDetail.setDiscRebateAmt(couponVal);	
			vendorInvoiceHeader.addDetail(vendorInvoiceDetail);
		}
		
		vendorInvoiceDetail = new VendorInvoiceDetailsVO();
		vendorInvoiceDetail.setRecordType("D");
		vendorInvoiceDetail.setUnitCost(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("unitCost"), 2));
		vendorInvoiceDetail.setTotalCost(MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("totalCost"), 2));
		String qty = "1.00";
		if (!MALUtilities.isEmpty(fieldSet.readString("qty"))) {
			qty = MALUtilities.parseDecimalNumberAndRound(fieldSet.readString("qty"), 2);
		}
		vendorInvoiceDetail.setQty(qty);
		vendorInvoiceDetail.setPartServiceDesc(fieldSet.readString("partServiceDesc"));
		
		ServiceProvider parentProvider = serviceProviderService.getServiceProvider(parentProviderId);
		if(parentProvider != null){
			List<ServiceProviderMaintenanceCode> maintCodes = maintCodeService.getServiceProviderCodeByDescription(
					fieldSet.readString("partServiceDesc"), this.parentProviderId);
						
			if(!MALUtilities.isEmpty(maintCodes) && maintCodes.size() > 0){
				vendorInvoiceDetail.setPartServiceCode(maintCodes.get(0).getCode());
			}else{
				// flagged the repair for review
				// save the maintenance code in supp_maint_code table
				ServiceProviderMaintenanceCode vendorCode = new ServiceProviderMaintenanceCode();		
				vendorCode.setServiceProvider(parentProvider);
				vendorCode.setDescription(vendorInvoiceDetail.getPartServiceDesc());
				try {
					maintCodeService.saveServiceProviderMaintCode(vendorCode);
				} catch (MalBusinessException e) {
					logger.error(e, "Error occured while auto-adding a valvoline Vendor Maint Code while processing Invoices");
				}
			}

		}		
		vendorInvoiceHeader.addDetail(vendorInvoiceDetail);
		
		return vendorInvoiceHeader;
	}
	
	public void setParentProviderId(Long parentProviderId) {
		this.parentProviderId = parentProviderId;
	}
}
