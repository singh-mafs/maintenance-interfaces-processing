package com.mikealbert.data;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.mikealbert.data.entity.ServiceProvider;
import com.mikealbert.service.LookupCacheService;
import com.mikealbert.service.ServiceProviderService;

public class ServiceProviderMatchingListFactory implements MatchingListFactory {
	@Resource LookupCacheService cache;
	ServiceProviderService serviceProviderService;
	
	private Long parentProviderId;
	
	List<ServiceProvider> providers;

	@Value("#{jobParameters[parentProviderId]}")
	public void setParentProviderId(Long parentProviderId) {
		this.parentProviderId = parentProviderId;
	}

	@Override
	public List<?> getMatchingList() {
		providers = cache.getServiceProviders(this.parentProviderId);
		return providers;
	}

}
