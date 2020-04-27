package com.ttulka.ecommerce.sales.cart;

/**
 * Retrieve Cart use-case.
 */
public interface RetrieveCart {

    /**
     * Retrieve cart by ID.
     *
     * @param cartId the cart ID
     * @return the cart
     */
    Cart byId(CartId cartId);
}
