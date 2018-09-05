package com.mikealbert.batch.mappers;

public class DeliveringDealerMapper {
	
	
	
	private String makeCode,area,areaDescription,contactName,tedContactPhone,email,dealerName,address,city,state,zip,dealerPhone, fax ;
	private String supplierExistsYN, ncvBatchYN, cvBatchYN, processedFileName, region, district;
	private Long makeId;
	

	 @Override
	   public String toString() {
	        return ("makeCode: "+this.getMakeCode()+
	                    " dealerName: "+ this.getDealerName() +
	                    " address: "+ this.getAddress() +
	                    " city : " + this.getCity() +
	                    " state : " + this.getState() +
	                    " postalCode : " + this.getZip()
	                    );
	   }
	 
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getMakeCode() {
		return makeCode;
	}
	public void setMakeCode(String makeCode) {
		this.makeCode = makeCode;
	}
	public String getProcessedFileName() {
		return processedFileName;
	}
	public void setProcessedFileName(String processedFileName) {
		this.processedFileName = processedFileName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAreaDescription() {
		return areaDescription;
	}
	public void setAreaDescription(String areaDescription) {
		this.areaDescription = areaDescription;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getTedContactPhone() {
		return tedContactPhone;
	}
	public void setTedContactPhone(String tedContactPhone) {
		this.tedContactPhone = tedContactPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getDealerPhone() {
		return dealerPhone;
	}
	public void setDealerPhone(String dealerPhone) {
		this.dealerPhone = dealerPhone;
	}
	
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public Long getMakeId() {
		return makeId;
	}
	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}
	public String getSupplierExistsYN() {
		return supplierExistsYN;
	}
	public void setSupplierExistsYN(String supplierExistsYN) {
		this.supplierExistsYN = supplierExistsYN;
	}
	public String getNcvBatchYN() {
		return ncvBatchYN;
	}
	public void setNcvBatchYN(String ncvBatchYN) {
		this.ncvBatchYN = ncvBatchYN;
	}
	public String getCvBatchYN() {
		return cvBatchYN;
	}
	public void setCvBatchYN(String cvBatchYN) {
		this.cvBatchYN = cvBatchYN;
	}
	
}