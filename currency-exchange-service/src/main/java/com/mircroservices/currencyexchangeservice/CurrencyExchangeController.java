package com.mircroservices.currencyexchangeservice;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.mircroservices.currencyexchangeservice.model.CurrencyExchangeRate;
import com.mircroservices.currencyexchangeservice.model.OnlineCurrencyExchange;
import com.mircroservices.currencyexchangeservice.repo.CurrencyExchangeRateRepo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CurrencyExchangeController {

	/*
	 * @Value("${currency-exchange.online-service-url}") String url;
	 */
	
	private Logger log=LoggerFactory.getLogger(CurrencyExchangeController.class);
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
	
	@GetMapping("/sample")
//	@Retry(name = "sample-api" ,fallbackMethod = "fallBackResponse")
	@RateLimiter(name = "samplelimit")
	public String sampleApi() {
		
		log.info("sample api ++");
		//new RestTemplate().getForObject("gtt;", String.class);
		
		return "success";
	}
	
	@GetMapping("/{from}/{to}")
	@Retry(name="get-currency-rate",fallbackMethod = "getCurrencyRateFallback")
	@CircuitBreaker(name="get-currency-rate",fallbackMethod = "getCurrencyRateFallback")
	@RateLimiter(name = "get-currency-rate")
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
	@Retry(name="get-currency-rate",fallbackMethod = "getCurrencyRateFallback")
	@CircuitBreaker(name="get-currency-rate",fallbackMethod = "getCurrencyRateFallback")
	@RateLimiter(name = "get-currency-rate")
	public CurrencyExchangeRate getCurrencyRateThroughFeign(@PathVariable String from ,@PathVariable String to){
		
		log.info("getCurrencyRateThroughFeign++");
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
	
	public String fallBackResponse(Throwable e) {
		
		return "fallBackResponse for sample-api";
	}
	
	
	
	//fallback responses
	
	public CurrencyExchangeRate getCurrencyRateFallback(Exception e) {
		
		CurrencyExchangeRate resp=new CurrencyExchangeRate();
		resp.setFrom("NA");
		resp.setTo("NA");
		resp.setConversionValue(BigDecimal.valueOf(0));
		resp.setId(0);
		resp.setEnvironment("Fallback response for getCurrencyRate");
		return resp;
	}
}
