package com.mircroservices.currencyconversionservice.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CurrencyConversion {
	
	@JsonIgnore
	private int id;
	private String from;
	private String to;
	private BigDecimal conversionValue;
	private String environment;
	private BigDecimal calculatedAmount;
	@JsonIgnore
	private BigDecimal amount;
	private String quantity;
	
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

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public BigDecimal getCalculatedAmount() {
		return calculatedAmount;
	}

	public void setCalculatedAmount(BigDecimal calculatedAmount) {
		this.calculatedAmount = calculatedAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public CurrencyConversion(int id, String from, String to, BigDecimal conversionValue,
			BigDecimal amount,String quantity,BigDecimal calculatedAmount,String environment) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.conversionValue = conversionValue;
		this.calculatedAmount = calculatedAmount;
		this.amount = amount;
		this.quantity=quantity;
		this.environment = environment;
	}

	public CurrencyConversion() {

	}

}
