package com.ttulka.ecommerce.sales.cart.jdbc;

import com.ttulka.ecommerce.sales.cart.Cart;
import com.ttulka.ecommerce.sales.cart.CartId;
import com.ttulka.ecommerce.sales.cart.RetrieveCart;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * JDBC implementation for Retrieve Cart use-cases.
 */
@RequiredArgsConstructor
class RetrieveCartJdbc implements RetrieveCart {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public Cart byId(CartId cartId) {
        return new CartJdbc(cartId, jdbcTemplate);
    }
}
