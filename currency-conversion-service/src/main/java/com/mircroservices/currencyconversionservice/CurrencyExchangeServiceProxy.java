package com.mircroservices.currencyconversionservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mircroservices.currencyconversionservice.model.CurrencyConversion;

//@FeignClient(name="currency-exchange",url="localhost:8000")
@FeignClient(name="currency-exchange")
public interface CurrencyExchangeServiceProxy {

	@GetMapping("currency-exchange/fromDb/{from}/{to}")
	public CurrencyConversion getCurrencyRate(@PathVariable String from ,@PathVariable String to);
	
	//@GetMapping("/")
	//public CurrencyConversion getCurrencyRateFromDB(@PathVariable String from ,@PathVariable String to);
		
}
