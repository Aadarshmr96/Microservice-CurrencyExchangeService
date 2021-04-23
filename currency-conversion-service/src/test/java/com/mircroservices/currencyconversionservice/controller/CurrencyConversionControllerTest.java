package com.mircroservices.currencyconversionservice.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.mircroservices.currencyconversionservice.CurrencyExchangeServiceProxy;
import com.mircroservices.currencyconversionservice.model.CurrencyConversion;

class CurrencyConversionControllerTest {

	private CurrencyConversion currencyConversion;
	
	private CurrencyConversionController underTest;
	
	@Mock
	private  CurrencyExchangeServiceProxy proxy;
	
	@BeforeEach
	void setup() {
		
		 currencyConversion=new CurrencyConversion(1,"usd","inr",BigDecimal.valueOf(74.6),BigDecimal.valueOf(74.6),"10",BigDecimal.valueOf(746.7),
				 "8000");
	}
	
	@Test
	@DisplayName("validate CurrencyConversion ")
	void getCurrencyRateTest() {
		
		Mockito.when(proxy.getCurrencyRate(Mockito.anyString(),Mockito.anyString())).thenReturn(currencyConversion);
		CurrencyConversion actual = underTest.getCurrencyRate("usd","inr",BigDecimal.valueOf(10));
		
		assertThat(currencyConversion).usingRecursiveComparison(actual);
	}

}
