package com.ttulka.ecommerce.catalogue.cart;

import javax.servlet.http.Cookie;

import com.ttulka.ecommerce.catalogue.cart.cookies.CartCookies;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CartCookiesTest {

    @Test
    void cart_is_empty() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        assertThat(cart.items()).isEmpty();
    }

    @Test
    void cart_item_is_created_from_cookies() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("cart", "test-1|Test_1|123"));

        Cart cart = new CartCookies(request, new MockHttpServletResponse());
        assertAll(
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productCode()).isEqualTo("test-1"),
                () -> assertThat(cart.items().get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(123))
        );
    }

    @Test
    void two_cart_items_are_created_from_cookies() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("cart", "test-1|Test_1|123#test-2|Test_2|321"));

        Cart cart = new CartCookies(request, new MockHttpServletResponse());
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
    void item_is_added() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        assertAll(
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productCode()).isEqualTo("test-1"),
                () -> assertThat(cart.items().get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(123))
        );
    }

    @Test
    void empty_cart_has_no_items() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        assertThat(cart.hasItems()).isFalse();
    }

    @Test
    void cart_has_items() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        assertThat(cart.hasItems()).isTrue();
    }

    @Test
    void quantity_is_increased() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        cart.add(new CartItem("test-1", "Test 1", new Quantity(321)));
        assertAll(
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productCode()).isEqualTo("test-1"),
                () -> assertThat(cart.items().get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(444))
        );
    }

    @Test
    void multiple_items_are_added() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        cart.add(new CartItem("test-2", "Test 2", new Quantity(321)));
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
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        cart.add(new CartItem("test-2", "Test 2", new Quantity(321)));

        cart.remove("test-1");
        assertAll(
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productCode()).isEqualTo("test-2"),
                () -> assertThat(cart.items().get(0).title()).isEqualTo("Test 2"),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(321))
        );
    }

    @Test
    void cart_is_emptied() {
        Cart cart = new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse());
        cart.add(new CartItem("test-1", "Test 1", new Quantity(123)));
        cart.add(new CartItem("test-2", "Test 2", new Quantity(321)));
        cart.empty();

        assertThat(cart.items()).isEmpty();
    }
}
