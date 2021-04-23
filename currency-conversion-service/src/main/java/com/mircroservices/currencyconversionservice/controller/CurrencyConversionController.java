package com.mircroservices.currencyconversionservice.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mircroservices.currencyconversionservice.CurrencyExchangeServiceProxy;
import com.mircroservices.currencyconversionservice.model.CurrencyConversion;

@RestController
public class CurrencyConversionController {

	@Autowired
	final static private Logger log=LoggerFactory.getLogger(CurrencyConversionController.class); 
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;	
	
	@GetMapping("/{from}/{to}/quantity/{amt}")
	public CurrencyConversion getCurrencyRate(@PathVariable String from ,@PathVariable String to ,@PathVariable BigDecimal amt) {
		log.info("Called =================");
		CurrencyConversion currencyRate = proxy.getCurrencyRate(from, to);
		log.info("currencyRate"+currencyRate);
		if(null!=currencyRate) {
			currencyRate.setQuantity(amt.toString()+" "+to.toUpperCase());
			
			return new CurrencyConversion(currencyRate.getId(),currencyRate.getFrom(),currencyRate.getTo(), currencyRate.getConversionValue(), 
					amt, currencyRate.getQuantity(),amt.multiply(currencyRate.getConversionValue()),currencyRate.getEnvironment());
		}else
			throw new RuntimeException();
	}
	
}
