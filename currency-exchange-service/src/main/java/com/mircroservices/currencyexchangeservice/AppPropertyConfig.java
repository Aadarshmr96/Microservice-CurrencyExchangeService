package com.mircroservices.currencyexchangeservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("currency-exchange")
public class AppPropertyConfig {
	
	private String OnlineServiceUrl;

	public String getOnlineServiceUrl() {
		return OnlineServiceUrl;
	}

	public void setOnlineServiceUrl(String onlineServiceUrl) {
		OnlineServiceUrl = onlineServiceUrl;
	}

	
}
