package com.example;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class CheckoutController {

	@Autowired CheckoutService svc;
	
	/**
	 * Get a specific product:
	 */
	@GetMapping("/checkout/{customer}")
	public BigDecimal getCheckoutPrice( @PathVariable String customer) throws Exception {
		return svc.calculateCheckoutPrice(customer);
	}

	@GetMapping("/checkout/{customer}/shipping/{shipping}")
	public BigDecimal getCheckoutPrice( @PathVariable String customer, @PathVariable String shipping) throws Exception {
		return svc.calculateCheckoutPrice(customer,shipping);
	}

	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(HttpClientErrorException.NotFound.class)
	public void notFound( ) {}
}
