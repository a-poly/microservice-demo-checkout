# Demo Microservice Example:  Checkout Service

This is an example microservice that calls a number of other services to calculate the total checkout price for a given customer and shipping option.

The essential algorithm: find all items in the customer's cart, get the sales price (including tax) for each item, get the shipping price for each item, and add these values together.

It calls the cart service to get the items in the cart.  Each item has a product code and quantity.

Then it calls the pricing service to get the sales price for each product (which includes tax).  

Then it calls the shipping service to get the shipping cost for each product.

Adding all of these values together yields the total checkout price.



This code also demonstrates:
- Basic usage of Spring Boot
- Spring Cloud is included, but not really used.
- Spring Cloud AWS is included, but not really used.
- Runs nicely on or off AWS.
- Actuator endpoints are present.
