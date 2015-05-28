package com.mikealbert.data.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nu.xom.MalformedURIException;

import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import com.mikealbert.common.MalConstants;
import com.mikealbert.data.entity.CityZipCode;
import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.data.entity.ServiceProviderAddress;
import com.mikealbert.data.entity.ServiceProviderDiscount;
import com.mikealbert.data.vo.StoreLocationVO;
import com.mikealbert.service.AddressService;
import com.mikealbert.service.ServiceProviderService;
import com.mikealbert.util.MALUtilities;

@Component("storeToProviderMappingHelper")
public class StoreToProviderMappingHelper {
	
	public static final String AUTO_EMPLOYEE_NO = "AUTO";
	public static final String POST_ADDRESS_TYPE = "POST";
	
	@Resource ServiceProviderService serviceProviderService;
	@Resource AddressService addressService;
	
	public void mapParentAttributes(ServiceProvider provider, ServiceProvider parent){
		provider.setParentServiceProvider(parent);
		provider.setPayeeAccount(parent.getPayeeAccount());
		provider.setNetworkVendor(parent.getNetworkVendor());
		provider.setServiceProviderCategory(parent.getServiceProviderCategory());
		provider.setServiceTypeCode(parent.getServiceTypeCode());
	}

	public void mapCommonAttributes(ServiceProvider provider, StoreLocationVO storeLocation){
		// copy from-parent and common attributes
		provider.setServiceProviderName(storeLocation.getStoreName());
		// store code needs to always be uppercase per Willow limitations
		// underscores or hypens needs to be stripped in willow data base
		
		provider.setServiceProviderNumber(reformatStoreCode(storeLocation.getStoreCode())); 
		provider.setInactiveInd(MalConstants.FLAG_N);
		provider.setNoOfBays(storeLocation.getNumberOfBays());
		provider.setWorkshopClearanceFeet(formatMaxClearance(storeLocation.getClearanceInFeet()));
		provider.setNormalHoursWeek(formatStartEndTimeAsStoreHours(storeLocation.getWeekdayStartTime(), storeLocation.getWeekdayEndTime()));
		provider.setNormalHoursSat(formatStartEndTimeAsStoreHours(storeLocation.getSaturdayStartTime(), storeLocation.getSaturdayEndTime()));
		provider.setNormalHoursSun(formatStartEndTimeAsStoreHours(storeLocation.getSundayStartTime(), storeLocation.getSundayEndTime()));
		provider.setTelephoneNo(storeLocation.getTelephoneNumber() != null ? reformatPhoneNumber(storeLocation.getTelephoneNumber()):"");
		provider.setFaxNo(storeLocation.getFaxNumber() != null ? reformatPhoneNumber(storeLocation.getFaxNumber()):"");
		provider.setEmailAddress(storeLocation.getEmailAddress());
	}
	
	public void mapAddressAttributes(ServiceProviderAddress providerAddress, ServiceProvider parent, StoreLocationVO storeLocation){
		// address
		providerAddress.setAddressLine1(storeLocation.getAddressLine1());
		// there can be address lines 2,3,4 that are set to "null/empty" with an update.
		// so we should take anything that is passed in and set it on the object.
		providerAddress.setAddressLine2(storeLocation.getAddressLine2());
		providerAddress.setAddressLine3(storeLocation.getAddressLine3());
		providerAddress.setAddressLine4(storeLocation.getAddressLine4());
		// this route determine geo location and updates the address information appropriately
		providerAddress.setGeoCode(determineGeoCode(storeLocation));
		
		providerAddress.setRegion(storeLocation.getStateProv());
		providerAddress.setPostcode(storeLocation.getZipCode());
		providerAddress.setTownCity(storeLocation.getCity().toUpperCase());
		
		providerAddress.setRegion(storeLocation.getStateProv());
		providerAddress.setPostcode(storeLocation.getZipCode());
		providerAddress.setTownCity(storeLocation.getCity().toUpperCase());
		providerAddress.setCountryCode(parent.getServiceProviderAddresses().get(0).getCountryCode());
		providerAddress.setCountyCode(storeLocation.getCountyCode());
	}
	
	public void mapDiscounts(ServiceProvider provider, ServiceProvider parent){
		List<ServiceProviderDiscount> discs = createCopyOfDiscounts(parent.getServiceProviderDiscounts());
		for(ServiceProviderDiscount disc : discs){
			disc.setServiceProvider(provider);
		}
		provider.setServiceProviderDiscounts(discs);
	}	
	
	public ServiceProvider getParentFromProperties(@Headers Map<String, Object> headers){
		return serviceProviderService.getServiceProvider(getParentProviderId(headers));
	}
	
	
	public String reformatStoreCode(String storeCode){
		return storeCode.replace("_","").replace("-","").toUpperCase();
	}
	
	public String reformatPhoneNumber(String phoneNumber) {
		if (phoneNumber != null) {
			if (phoneNumber.contains("-")) {
				if (phoneNumber.startsWith("(")) {
					phoneNumber = phoneNumber.replaceAll("[()]", "_");
					phoneNumber = phoneNumber.substring(1).replaceAll("_", "-");
					phoneNumber = phoneNumber.replaceAll(" ", "");
				}
			}// else it's in the format we want to be in 
				else if (!MALUtilities.isEmpty(phoneNumber )) {
					// TODO: add hyphens to the phone number
					StringBuilder ahphoneNumber = new StringBuilder(phoneNumber)
							.insert(3, "-").insert(7, "-");
					phoneNumber = ahphoneNumber.toString();
				}
			}
		return phoneNumber;
	}

	private Long getParentProviderId(@Headers Map<String, Object> headers){
		// get the parent Id from the exchange properties
	    Long parentId = null;
	    Object parentIdProp = headers.get("parentProviderId");
	    if (parentIdProp != null) {
	    	parentId = Long.valueOf(parentIdProp.toString());
	    }

	    return parentId;
	}
	
	private List<ServiceProviderDiscount> createCopyOfDiscounts(List<ServiceProviderDiscount> parentDiscounts){
		List<ServiceProviderDiscount> discountsCopy = new ArrayList<ServiceProviderDiscount>();
		ServiceProviderDiscount discountCopy;
		for(ServiceProviderDiscount parentDiscount : parentDiscounts){
			discountCopy = new ServiceProviderDiscount();
						
			discountCopy.setPartsDisc(parentDiscount.getPartsDisc());
			discountCopy.setLabourDisc(parentDiscount.getLabourDisc());
			discountCopy.setTyreDisc(parentDiscount.getTyreDisc());
			discountCopy.setDiscAppl(parentDiscount.getDiscAppl());
			discountCopy.setReviewDate(parentDiscount.getReviewDate());
			discountCopy.setEffectiveDate(parentDiscount.getEffectiveDate());
			// TODO: fix core-data to make this work
			discountCopy.setMake(parentDiscount.getMake());
			discountCopy.setMakeModelRange(parentDiscount.getMakeModelRange());
			discountCopy.setModelType(parentDiscount.getModelType());
			
			discountsCopy.add(discountCopy);
		}
		
		return discountsCopy;
	}
	
	private String formatMaxClearance(Long maxClearance){
		StringBuffer maxClearanceBuff = new StringBuffer();
		if((!MALUtilities.isEmpty(maxClearance)) && maxClearance > 0){
			maxClearanceBuff.append(maxClearance);
			maxClearanceBuff.append(" ");
			maxClearanceBuff.append("feet");
		}
		return maxClearanceBuff.toString();
	}
	
	private String formatStartEndTimeAsStoreHours(String startTime, String endTime){
		StringBuffer storeHoursBuff = new StringBuffer();
		if(MALUtilities.isNotEmptyString(startTime) && MALUtilities.isNotEmptyString(endTime)){
			storeHoursBuff.append(startTime);
			storeHoursBuff.append(" ");
			storeHoursBuff.append("to");
			storeHoursBuff.append(" ");
			storeHoursBuff.append(endTime);
			storeHoursBuff.trimToSize();			
		}
		return storeHoursBuff.toString();
	}
	
	private String determineGeoCode(StoreLocationVO storeLocation){
		List<CityZipCode> cityZipCodes = new ArrayList<CityZipCode>();
		if(storeLocation.getZipCode().length() > 5){
			cityZipCodes = addressService.getAllCityZipCodesByZipCode(storeLocation.getZipCode().split("-")[0]);
		}else{
			cityZipCodes = addressService.getAllCityZipCodesByZipCode(storeLocation.getZipCode());
		}
		
		String geoCode = null;
		for(CityZipCode cityZipCode : cityZipCodes){
			// check to see if countryCode is set; if it is then match on zip and county code and townCity
			if(MALUtilities.isNotEmptyString(storeLocation.getCountyCode())){
				if(cityZipCode.getTownCityCode().getCountyCode().getCountyCodesPK().getCountyCode().equalsIgnoreCase(storeLocation.getCountyCode())){
					if(cityZipCode.getTownCityCode().getTownCityCodesPK().getTownName().equalsIgnoreCase(storeLocation.getCity())){
						String region = cityZipCode.getTownCityCode().getRegionCode().getShortCode();
						String county = cityZipCode.getTownCityCode().getTownCityCodesPK().getCountyCode();
						String geo = cityZipCode.getGeoCode();
						//Set the additional information to out tax/geo data we find by the zip code and town name.
						storeLocation.setStateProv(cityZipCode.getTownCityCode().getTownCityCodesPK().getRegionCode());
						storeLocation.setCountyCode(county);
						geoCode = region + "-" + county + "-" + geo;
						break;
					}
				}
			}else{ // otherwise match on zip and townCity name
				if(cityZipCode.getTownCityCode().getTownCityCodesPK().getTownName().equalsIgnoreCase(storeLocation.getCity())){
					String region = cityZipCode.getTownCityCode().getRegionCode().getShortCode();
					String county = cityZipCode.getTownCityCode().getTownCityCodesPK().getCountyCode();
					String geo = cityZipCode.getGeoCode();
					//Set the additional information to out tax/geo data we find by the zip code and town name.
					storeLocation.setStateProv(cityZipCode.getTownCityCode().getTownCityCodesPK().getRegionCode());
					storeLocation.setCountyCode(county);
					geoCode = region + "-" + county + "-" + geo;
					break;
				}
			}
		}
		
		return geoCode;
	}
}
