package com.ttulka.ecommerce.catalogue;

import com.ttulka.ecommerce.catalogue.cart.Cart;

/**
 * Remove Cart Item use-case.
 */
public interface RemoveCartItem {

    /**
     * Removes the item from the cart.
     *
     * @param cart        the cart
     * @param productCode the product code of the item
     */
    void fromCart(Cart cart, String productCode);
}
