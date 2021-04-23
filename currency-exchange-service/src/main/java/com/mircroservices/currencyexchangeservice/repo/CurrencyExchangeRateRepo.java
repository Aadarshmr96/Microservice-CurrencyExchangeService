package com.mircroservices.currencyexchangeservice.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mircroservices.currencyexchangeservice.model.CurrencyExchangeRate;

@Repository
public interface CurrencyExchangeRateRepo extends JpaRepository<CurrencyExchangeRate,Integer>{

	CurrencyExchangeRate findByFromAndTo(String from ,String to);
	
	@Query("select case when count(s) >0 then true else false end "
			+ "from CurrencyExchangeRate s where s.from=?1"
			)
	Boolean selectExistsFrom(String from);
}
