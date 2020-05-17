package com.ttulka.ecommerce.sales.cart;

import java.util.List;

import com.ttulka.ecommerce.sales.cart.item.CartItem;

/**
 * List Cart Items use-case.
 */
public interface ListCartItems {

    /**
     * Lists items in the cart.
     *
     * @param cartId the cart ID
     * @return items in the cart
     */
    List<CartItem> listCart(CartId cartId);
}
