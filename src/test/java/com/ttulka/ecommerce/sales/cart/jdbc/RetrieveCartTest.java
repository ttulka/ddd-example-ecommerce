package com.ttulka.ecommerce.sales.cart.jdbc;

import com.ttulka.ecommerce.sales.cart.Cart;
import com.ttulka.ecommerce.sales.cart.CartId;
import com.ttulka.ecommerce.sales.cart.RetrieveCart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = CartJdbcConfig.class)
class RetrieveCartTest {

    @Autowired
    private RetrieveCart retrieveCart;

    @Test
    void cart_is_retrieved() {
        Cart cart = retrieveCart.byId(new CartId("TEST"));
        assertThat(cart.id()).isEqualTo(new CartId("TEST"));
    }
}
