package com.mircroservices.currencyexchangeservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.mircroservices.currencyexchangeservice.model.CurrencyExchangeRate;
import com.mircroservices.currencyexchangeservice.repo.CurrencyExchangeRateRepo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class CurrencyExchangeControllerTest {

	@LocalServerPort
	int randomServerPort;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private CurrencyExchangeController underTest;
	
	@Mock
	private CurrencyExchangeRateRepo repo;
	
	@Mock
	private Environment env;
	
	
	private AutoCloseable closeable;
	
	 @Captor
	private ArgumentCaptor<CurrencyExchangeRate> currencyArgumentCaptor;
	 
	private CurrencyExchangeRate currencyExchangeRate;
	@BeforeEach
	void setup() {
		
		currencyExchangeRate=new CurrencyExchangeRate(1,"usd","inr",BigDecimal.valueOf(75.3624),"8000");
		closeable = MockitoAnnotations.openMocks(this);
		underTest=new CurrencyExchangeController(repo,env);
	}
	
	
	@AfterEach
	void tearDown() throws Exception
	{
		closeable.close();
		
	}
	@Test
	@DisplayName("validate getCurrencyRate")
	void testGetCurrencyRate() throws Exception {
		
	
		CurrencyExchangeRate currencyExchangeRateAud=new CurrencyExchangeRate(1,"usd","aud",BigDecimal.valueOf(1.2925),"8000");
		CurrencyExchangeRate currencyExchangeRateEur=new CurrencyExchangeRate(1,"usd","eur",BigDecimal.valueOf(0.8306),"8000");
		
		
		//String url="http://localhost:"+randomServerPort+"/currency-exchange/usd/inr";
		
		List<String> urls=null;
		String inrUrl="http://localhost:"+randomServerPort+"/currency-exchange/usd/inr";
		String audUrl="http://localhost:"+randomServerPort+"/currency-exchange/usd/aud";
		String eurUrl="http://localhost:"+randomServerPort+"/currency-exchange/usd/eur";
		
		CurrencyExchangeRate actualInr=this.restTemplate.getForObject(new URI(inrUrl),CurrencyExchangeRate.class);
		CurrencyExchangeRate actualAud=this.restTemplate.getForObject(new URI(audUrl),CurrencyExchangeRate.class);
		CurrencyExchangeRate actualEur=this.restTemplate.getForObject(new URI(eurUrl),CurrencyExchangeRate.class);
		assertThat(currencyExchangeRate.getConversionValue()).isEqualTo(actualInr.getConversionValue());
		assertThat(currencyExchangeRateAud.getConversionValue()).isEqualTo(actualAud.getConversionValue());
		assertThat(currencyExchangeRateEur.getConversionValue()).isEqualTo(actualEur.getConversionValue());
		
	}

	@Test
	void testGetCurrencyRateThroughFeign() {
		
		
	}

	@Test
	@DisplayName("Add Currency rate test")
	void testAddNewCurrency() throws URISyntaxException {
		
		String url="http://localhost:"+randomServerPort+"/currency-exchange/newCurrency";
		URI uri=new URI(url);
		
		HttpEntity<CurrencyExchangeRate> request=new HttpEntity<>(currencyExchangeRate);
		ResponseEntity<String> result=this.restTemplate.postForEntity(uri, request,String.class);
		
		assertThat(201).isEqualTo(result.getStatusCodeValue());
	}

	@Test
	@DisplayName("fetch currency Rate from DB")
	void testGetCurrencyRateFromDB() {
		
		//CurrencyExchangeRate currencyExchangeRateActual=new CurrencyExchangeRate(1,"usd","inr",BigDecimal.valueOf(74.3),"8000");
		//when
		Mockito.when(repo.findByFromAndTo(currencyExchangeRate.getFrom(),currencyExchangeRate.getTo())).thenReturn(currencyExchangeRate);
		String port="8000";
		Mockito.when(env.getProperty("local.server.port")).thenReturn(port);
		underTest.getCurrencyRateFromDB(currencyExchangeRate.getFrom(),currencyExchangeRate.getTo());
		
		//then
		//ArgumentCaptor<String> currencyExchangeRateargs=ArgumentCaptor.forClass(String.class);
		
		verify(repo,times(1)).findByFromAndTo(currencyExchangeRate.getFrom(),currencyExchangeRate.getTo());
//		verify(repo,times(1)).findByFromAndTo(currencyArgumentCaptor.capture(),currencyArgumentCaptor.capture().getTo());
	//	assertThat
		//String captureRate=currencyExchangeRateargs.getValue();
		//assertThat(currencyArgumentCaptor.getValue().getConversionValue().equals(currencyExchangeRate.getConversionValue()));
		
	}
	

	@Test
	@DisplayName("throw runtimeException when no records in DB")
	void validateNegativeTesttoFetchRecordsfromDB() throws URISyntaxException {
		
		Mockito.when(repo.findByFromAndTo(currencyExchangeRate.getFrom(),currencyExchangeRate.getTo())).thenReturn(null);
		String port="8000";
		Mockito.when(env.getProperty("local.server.port")).thenReturn(port);
		//CurrencyExchangeRate actual = underTest.getCurrencyRateFromDB("usd","cad");
		
		assertThatThrownBy(()->underTest.getCurrencyRateFromDB("usd","cad")).isInstanceOf(RuntimeException.class);
	
	}
	
	@Test
	@DisplayName("Negative case for Add currency")
	void validateNegativecaseforAddNewCurrency() throws URISyntaxException {
		assertThatThrownBy(()->underTest.addNewCurrency(null)).isInstanceOf(RuntimeException.class);
	}
	
	@Test
	@DisplayName("Negative case for getCurrencyRateFromDB")
	void validateNegativecaseforgetCurrencyRateFromDB() throws URISyntaxException {
		
		Mockito.when(repo.findByFromAndTo(Mockito.anyString(),Mockito.anyString())).thenReturn(null);
		assertThatThrownBy(()->underTest.getCurrencyRateFromDB("inr","aud")).isInstanceOf(RuntimeException.class);
		
	}
}
