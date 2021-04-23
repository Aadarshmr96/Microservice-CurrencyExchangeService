package com.mircroservices.currencyexchangeservice.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CurrencyExchangeRate {

	@Id
	//@GeneratedValue
	private int id;
	
	@Column(name = "Currency_from")
	private String from;
	
	@Column(name = "Currency_to")
	private String to;
	private BigDecimal conversionValue;
	private String environment;
	
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public BigDecimal getConversionValue() {
		return conversionValue;
	}
	public void setConversionValue(BigDecimal conversionValue) {
		this.conversionValue = conversionValue;
	}
	
	public CurrencyExchangeRate(int id, String from, String to, BigDecimal conversionValue, String environment) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.conversionValue = conversionValue;
		this.environment = environment;
	}
	public CurrencyExchangeRate() {
		super();
	}
	
	
	
	
}
