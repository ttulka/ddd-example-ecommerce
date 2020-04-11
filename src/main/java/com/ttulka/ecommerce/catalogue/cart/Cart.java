package com.ttulka.ecommerce.catalogue.cart;

import java.util.List;

/**
 * Cart entity.
 */
public interface Cart {

    List<CartItem> items();

    boolean hasItems();

    void add(CartItem toAdd);

    void remove(String productCode);

    void empty();
}
