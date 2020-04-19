package com.ttulka.ecommerce.catalogue;

import java.util.UUID;

import javax.servlet.http.Cookie;

import com.ttulka.ecommerce.catalogue.cart.cookies.CartCookies;
import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.PrepareDelivery;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@ContextConfiguration(classes = {CatalogueConfig.class, PlaceOrderFromCartTest.ServicesTestConfig.class})
@Sql(statements = "INSERT INTO products VALUES ('1', 'test-1', 'Test 1', 'Test', 1.00), (2, 'test-2', 'Test 2', 'Test', 2.00);")
class PlaceOrderFromCartTest {

    @Autowired
    private PlaceOrderFromCart placeOrderFromCart;

    @Test
    void order_is_placed() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("cart", "test-1|Test_1|123"));

        placeOrderFromCart.placeOrder(UUID.randomUUID(),
                new CartCookies(request, new MockHttpServletResponse()));
    }

    @Test
    void empty_cart_throws_an_exception() {
        assertThrows(PlaceOrderFromCart.NoItemsToOrderException.class,
                     () -> placeOrderFromCart.placeOrder(UUID.randomUUID(),
                             new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse())));
    }

    @Configuration
    @ComponentScan("com.ttulka.ecommerce.sales")
    static class ServicesTestConfig {
        @MockBean
        private PrepareDelivery prepareDelivery;
        @MockBean
        private Warehouse warehouse;
        @MockBean
        private EventPublisher eventPublisher;
    }
}
