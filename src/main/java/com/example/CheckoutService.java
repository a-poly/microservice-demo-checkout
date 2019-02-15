package com.example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CheckoutService {

	@Value("${cart.service.url}") String cartServiceUrl;
	@Value("${pricing.service.url}") String pricingServiceUrl;
	@Value("${shipping.service.url}") String shippingServiceUrl;
	
	RestTemplate cartService = new RestTemplate();
	RestTemplate pricingService = new RestTemplate();
	RestTemplate shippingService = new RestTemplate();
	

	public BigDecimal calculateCheckoutPrice(String customer) throws Exception {
		return calculateCheckoutPrice(customer,"STANDARD");
	}
		
	/**
	 * Calculate the total checkout price for a given customer and shipping option.  The essential algorithm: 
	 * find all items in the customer's cart, get the sales price (including discounts & tax) for each item, 
	 * get the shipping price for each item, and add these values together.
	 * 
	 * The cart service is used to get the items in the cart.  Each item has a product code and quantity.
	 * The pricing service is used to get the sales price for each product (which includes tax).  
	 * The shipping service is used to get the shipping cost for each product.
	 */	
	public BigDecimal calculateCheckoutPrice(String customer,String shippingType) throws Exception {
			
		List<Item> items = lookupCart(customer);
		BigDecimal total = new BigDecimal(0);
		
		//	TODO: Make it nice, parallel, and reactive later:
		for (Item item : items) {
			BigDecimal price = lookupPriceIncludingTax(item.getCode());
			BigDecimal shippingCost = lookupShipping(item.getCode(),shippingType);
			total = total.add(price).add(shippingCost);
		}
		
		return total;
	}

	
	/**
	 * Obtain the customer's shopping cart.
	 */
	public List<Item> lookupCart(String customer) {
		Item[] items = cartService.getForObject(cartServiceUrl + "/carts/{customer}", Item[].class, customer);
		return Arrays.asList(items);
	}

	/**
	 * Obtain the product's price including tax from the pricing service.
	 */
	public BigDecimal lookupPriceIncludingTax(String code) {
		Item item = pricingService.getForObject(pricingServiceUrl + "/pricings/{code}", Item.class, code);
		return item.getPrice();  // This single value from the pricing service includes discounts and tax 
	}

	
	/**
	 * Obtain the shipping price for a product and the type of requested shipping.
	 */
	public BigDecimal lookupShipping(String code, String shippingType) {
		Item item = 
			shippingService.getForObject(
				shippingServiceUrl + "/shippings/{code}/type/{type}", 
				Item.class, code, shippingType);
		
		return item.getShippingCost();
	}

	
}
