package com.ttulka.ecommerce.catalogue;

import java.util.List;

import com.ttulka.ecommerce.catalogue.cart.Cart;
import com.ttulka.ecommerce.catalogue.cart.CartItem;
import com.ttulka.ecommerce.catalogue.cart.Quantity;
import com.ttulka.ecommerce.catalogue.cart.cookies.CartCookies;
import com.ttulka.ecommerce.common.events.EventPublisher;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = {CatalogueConfig.class, CartItemsTest.ServicesTestConfig.class})
@Sql(statements = "INSERT INTO products VALUES ('1', 'test-1', 'Test 1', 'Test', 1.00), (2, 'test-2', 'Test 2', 'Test', 2.00);")
class CartItemsTest {

    @Autowired
    private ListCartItems listCartItems;
    @Autowired
    private AddCartItem addCartItem;
    @Autowired
    private RemoveCartItem removeCartItem;

    @Test
    void item_is_listed() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        List<CartItem> items = listCartItems.listCart(cart);
        assertAll(
                () -> assertThat(items).hasSize(1),
                () -> assertThat(items.get(0).productCode()).isEqualTo("test-1"),
                () -> assertThat(items.get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(items.get(0).quantity()).isEqualTo(new Quantity(123))
        );
    }

    @Test
    void multiple_items_are_listed() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        cart.add(new CartItem("test-2", "Test 2", new Quantity(321)));
        List<CartItem> items = listCartItems.listCart(cart);
        assertAll(
                () -> assertThat(items).hasSize(2),
                () -> assertThat(items.get(0).productCode()).isEqualTo("test-1"),
                () -> assertThat(items.get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(items.get(0).quantity()).isEqualTo(new Quantity(123)),
                () -> assertThat(items.get(1).productCode()).isEqualTo("test-2"),
                () -> assertThat(items.get(1).title()).isEqualTo("Test 2"),
                () -> assertThat(items.get(1).quantity()).isEqualTo(new Quantity(321))
        );
    }

    @Test
    void item_is_added() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        addCartItem.intoCart(cart, "test-1", 123);
        assertAll(
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productCode()).isEqualTo("test-1"),
                () -> assertThat(cart.items().get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(123))
        );
    }

    @Test
    void multiple_items_are_added() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        addCartItem.intoCart(cart, "test-1", 123);
        addCartItem.intoCart(cart, "test-2", 321);
        assertAll(
                () -> assertThat(cart.items()).hasSize(2),
                () -> assertThat(cart.items().get(0).productCode()).isEqualTo("test-1"),
                () -> assertThat(cart.items().get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(123)),
                () -> assertThat(cart.items().get(1).productCode()).isEqualTo("test-2"),
                () -> assertThat(cart.items().get(1).title()).isEqualTo("Test 2"),
                () -> assertThat(cart.items().get(1).quantity()).isEqualTo(new Quantity(321))
        );
    }

    @Test
    void item_is_removed() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        addCartItem.intoCart(cart, "test-1", 123);
        addCartItem.intoCart(cart, "test-2", 456);
        removeCartItem.fromCart(cart, "test-1");
        assertAll(
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productCode()).isEqualTo("test-2"),
                () -> assertThat(cart.items().get(0).title()).isEqualTo("Test 2"),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(456))
        );
    }

    @Configuration
    @ComponentScan("com.ttulka.ecommerce.sales")
    static class ServicesTestConfig {
        @MockBean
        private Warehouse warehouse;
        @MockBean
        private EventPublisher eventPublisher;
    }
}
