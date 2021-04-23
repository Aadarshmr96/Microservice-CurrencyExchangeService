package com.mircroservices.apigateway;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

	@Bean
	public RouteLocator gatewayRoute(RouteLocatorBuilder builder) {
		
		//Function<PredicateSpec, Buildable<Route>> routeCurrencyExchange=p->p.path("/currency-exchange/**").uri("lb://currency-exchange");
		
		//Function<PredicateSpec, Buildable<Route>> routeCurrencyConversion=p->p.path("//currency-conversion/**").uri("lb://currency-conversion");
		return builder.routes().
				route(p->p.path("/currency-exchange/**").uri("lb://currency-exchange"))
				.route(p->p.path("/currency-conversion/**").uri("lb://currency-conversion"))
				.build();
	}
}
