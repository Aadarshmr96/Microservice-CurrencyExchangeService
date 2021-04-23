package com.mircroservices.currencyexchangeservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mircroservices.currencyexchangeservice.model.CurrencyExchangeRate;
import com.mircroservices.currencyexchangeservice.repo.CurrencyExchangeRateRepo;

@SpringBootTest
@DisplayName("Unit Testing CurrencyExchangeRateRepoTest ")
class CurrencyExchangeRateRepoTest {

	@Autowired
	private CurrencyExchangeRateRepo underTest;
	
	/*
	 * @Test void test() { fail("Not yet implemented"); }
	 */

	private CurrencyExchangeRate currencyExchangeRate;
	
	@BeforeEach
	void init(){
		
		currencyExchangeRate=new CurrencyExchangeRate(1,"usd","inr",BigDecimal.valueOf(74.3),"8000");
	}
	
	@DisplayName("check if From is present")
	@Test
	void itShouldCheckIfCurrencyFromExists() {
		
		//given
		
		//CurrencyExchangeRate currencyExchangeRate=new CurrencyExchangeRate(1,"usd","cad",BigDecimal.valueOf(1.3),"8000");
		
		//when
		
		boolean actual = underTest.selectExistsFrom(currencyExchangeRate.getFrom());
		
		//then
		assertThat(actual).isTrue();
		
	}
	
	@DisplayName("checkfindByFromAndTo")
	@Test
	void checkfindByFromAndTo(){
		
		//given 
		
		//when
		CurrencyExchangeRate findByFromAndTo = underTest.findByFromAndTo(currencyExchangeRate.getFrom(), currencyExchangeRate.getTo());
		
		//then
		
		assertThat(findByFromAndTo.equals(currencyExchangeRate));
	}
}
