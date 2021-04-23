package com.mircroservices.currencyexchangeservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.mircroservices.currencyexchangeservice.model.OnlineCurrencyExchange;

@FeignClient(name="v6.exchangerate-api.com",url="https://v6.exchangerate-api.com/v6/000908b5b1bb747564fe7688/latest/USD")
public interface OnlineCurrencyExchangeProxy {

	@GetMapping("")
	public OnlineCurrencyExchange getCurrencyRate();
	
}
