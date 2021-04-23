package com.mircroservices.currencyexchangeservice;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.mircroservices.currencyexchangeservice.repo.*;
import com.mircroservices.currencyexchangeservice.model.CurrencyExchangeRate;
import com.mircroservices.currencyexchangeservice.model.OnlineCurrencyExchange;

@RestController
public class CurrencyExchangeController {

	/*
	 * @Value("${currency-exchange.online-service-url}") String url;
	 */
	
	@Autowired
	private OnlineCurrencyExchangeProxy onlineCurrencyExchangeProxy; 
	
	@Autowired
	private AppPropertyConfig appPropertyConfig;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private CurrencyExchangeRateRepo CurrencyExchangeRateRepo;
	
	CurrencyExchangeController(CurrencyExchangeRateRepo repo,Environment env){
		this.CurrencyExchangeRateRepo=repo;
		this.env=env;
	}
	
	
	@GetMapping("/{from}/{to}")
	public CurrencyExchangeRate getCurrencyRate(@PathVariable String from ,@PathVariable String to){
		
		String url=null;
		System.out.println(appPropertyConfig.getOnlineServiceUrl());
		OnlineCurrencyExchange currencyExchange = new RestTemplate().getForObject(/*appPropertyConfig.getOnlineServiceUrl()*/url,OnlineCurrencyExchange.class);
		
		BigDecimal valueBigDecimal=null;
		switch(to.toLowerCase()) {
		case "inr":valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().iNR);
		break;
		case "aud":valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().aud);
		break;
		case "eur":valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().eur);
		break;
		//default:valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().usd);
		}
		
		 String instance = env.getProperty("local.server.port");
		 return new CurrencyExchangeRate(1,from,to,valueBigDecimal,instance);
	}
	
	@GetMapping("feign/{from}/{to}")
	public CurrencyExchangeRate getCurrencyRateThroughFeign(@PathVariable String from ,@PathVariable String to){
		
		OnlineCurrencyExchange currencyExchange = onlineCurrencyExchangeProxy.getCurrencyRate();
		BigDecimal valueBigDecimal=null;
		switch(to.toLowerCase()) {
		case "inr":valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().iNR);
		break;
		case "aud":valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().aud);
		break;
		case "eur":valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().eur);
		break;
		//default:valueBigDecimal=BigDecimal.valueOf(currencyExchange.getConversion_rates().usd);
		}
		String instance = env.getProperty("local.server.port");
		return new CurrencyExchangeRate(1,from,to,valueBigDecimal,instance);
	}
	
	@PostMapping("/newCurrency")
	public ResponseEntity addNewCurrency(@RequestBody CurrencyExchangeRate currencyExchangeRate) {
		
		if(null!=currencyExchangeRate) {
			CurrencyExchangeRateRepo.save(currencyExchangeRate);
			return new ResponseEntity(HttpStatus.CREATED);
		}else
			throw new RuntimeException();
	}
	
	@GetMapping("fromDb/{from}/{to}")
	public CurrencyExchangeRate getCurrencyRateFromDB(@PathVariable String from ,@PathVariable String to) {
		
		
		CurrencyExchangeRate currencyRate = CurrencyExchangeRateRepo.findByFromAndTo(from, to);
		if(null!=currencyRate) {
			String instance = env.getProperty("local.server.port");
			currencyRate.setEnvironment(instance);
			
			return currencyRate;
		}else {
			throw new RuntimeException();
		}
		
	}
}
