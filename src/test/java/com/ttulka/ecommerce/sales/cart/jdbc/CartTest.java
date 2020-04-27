package com.ttulka.ecommerce.sales.cart.jdbc;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.cart.Cart;
import com.ttulka.ecommerce.sales.cart.CartId;
import com.ttulka.ecommerce.sales.cart.item.CartItem;
import com.ttulka.ecommerce.sales.cart.item.ProductId;
import com.ttulka.ecommerce.sales.cart.item.Title;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = CartJdbcConfig.class)
@Sql(statements = {
        "INSERT INTO cart_items VALUES ('P001', 'Test 1', 12.3, 1, 'C001');",
        "INSERT INTO cart_items VALUES ('P002', 'Test 2', 10.5, 2, 'C001');"
})
class CartTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void cart_is_empty() {
        Cart cart = new CartJdbc(new CartId("WRONG"), jdbcTemplate);
        assertAll(
                () -> assertThat(cart.hasItems()).isFalse(),
                () -> assertThat(cart.items()).isEmpty()
        );
    }

    @Test
    void cart_has_items() {
        Cart cart = new CartJdbc(new CartId("C001"), jdbcTemplate);
        assertAll(
                () -> assertThat(cart.hasItems()).isTrue(),
                () -> assertThat(cart.items()).hasSize(2),
                () -> assertThat(cart.items().get(0).productId()).isEqualTo(new ProductId("P001")),
                () -> assertThat(cart.items().get(0).title()).isEqualTo(new Title("Test 1")),
                () -> assertThat(cart.items().get(0).unitPrice()).isEqualTo(new Money(12.3f)),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(1)),
                () -> assertThat(cart.items().get(1).productId()).isEqualTo(new ProductId("P002")),
                () -> assertThat(cart.items().get(1).title()).isEqualTo(new Title("Test 2")),
                () -> assertThat(cart.items().get(1).unitPrice()).isEqualTo(new Money(10.5f)),
                () -> assertThat(cart.items().get(1).quantity()).isEqualTo(new Quantity(2))
        );
    }

    @Test
    void item_is_added() {
        Cart cart = new CartJdbc(new CartId("CTEST"), jdbcTemplate);
        cart.add(new CartItem(new ProductId("PTEST"), new Title("Test"), new Money(1.5f), new Quantity(123)));
        assertAll(
                () -> assertThat(cart.hasItems()).isTrue(),
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productId()).isEqualTo(new ProductId("PTEST")),
                () -> assertThat(cart.items().get(0).title()).isEqualTo(new Title("Test")),
                () -> assertThat(cart.items().get(0).unitPrice()).isEqualTo(new Money(1.5f)),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(123))
        );
    }

    @Test
    void items_are_added() {
        Cart cart = new CartJdbc(new CartId("C001"), jdbcTemplate);
        cart.add(new CartItem(new ProductId("PTEST"), new Title("Test"), new Money(1.5f), new Quantity(123)));
        assertAll(
                () -> assertThat(cart.hasItems()).isTrue(),
                () -> assertThat(cart.items()).hasSize(3),
                () -> assertThat(cart.items().get(0).productId()).isEqualTo(new ProductId("P001")),
                () -> assertThat(cart.items().get(0).title()).isEqualTo(new Title("Test 1")),
                () -> assertThat(cart.items().get(0).unitPrice()).isEqualTo(new Money(12.3f)),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(1)),
                () -> assertThat(cart.items().get(1).productId()).isEqualTo(new ProductId("P002")),
                () -> assertThat(cart.items().get(1).title()).isEqualTo(new Title("Test 2")),
                () -> assertThat(cart.items().get(1).unitPrice()).isEqualTo(new Money(10.5f)),
                () -> assertThat(cart.items().get(1).quantity()).isEqualTo(new Quantity(2)),
                () -> assertThat(cart.items().get(2).productId()).isEqualTo(new ProductId("PTEST")),
                () -> assertThat(cart.items().get(2).title()).isEqualTo(new Title("Test")),
                () -> assertThat(cart.items().get(2).unitPrice()).isEqualTo(new Money(1.5f)),
                () -> assertThat(cart.items().get(2).quantity()).isEqualTo(new Quantity(123))
        );
    }

    @Test
    void quantity_is_increased() {
        Cart cart = new CartJdbc(new CartId("CTEST"), jdbcTemplate);
        cart.add(new CartItem(new ProductId("PTEST"), new Title("Test"), new Money(1.5f), new Quantity(123)));
        cart.add(new CartItem(new ProductId("PTEST"), new Title("Test"), new Money(1.5f), new Quantity(321)));
        assertAll(
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productId()).isEqualTo(new ProductId("PTEST")),
                () -> assertThat(cart.items().get(0).title()).isEqualTo(new Title("Test")),
                () -> assertThat(cart.items().get(0).unitPrice()).isEqualTo(new Money(1.5f)),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(444))
        );
    }

    @Test
    void multiple_items_are_added() {
        Cart cart = new CartJdbc(new CartId("CTEST"), jdbcTemplate);
        cart.add(new CartItem(new ProductId("PTEST"), new Title("Test"), new Money(1.5f), new Quantity(123)));
        cart.add(new CartItem(new ProductId("PTEST"), new Title("Test"), new Money(2.5f), new Quantity(123)));
        assertAll(
                () -> assertThat(cart.items()).hasSize(2),
                () -> assertThat(cart.items().get(0).productId()).isEqualTo(new ProductId("PTEST")),
                () -> assertThat(cart.items().get(0).title()).isEqualTo(new Title("Test")),
                () -> assertThat(cart.items().get(0).unitPrice()).isEqualTo(new Money(1.5f)),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(123)),
                () -> assertThat(cart.items().get(1).productId()).isEqualTo(new ProductId("PTEST")),
                () -> assertThat(cart.items().get(1).title()).isEqualTo(new Title("Test")),
                () -> assertThat(cart.items().get(1).unitPrice()).isEqualTo(new Money(2.5f)),
                () -> assertThat(cart.items().get(1).quantity()).isEqualTo(new Quantity(123))
        );
    }

    @Test
    void item_is_removed() {
        Cart cart = new CartJdbc(new CartId("C001"), jdbcTemplate);
        cart.remove(new ProductId("P001"));
        assertAll(
                () -> assertThat(cart.hasItems()).isTrue(),
                () -> assertThat(cart.items()).hasSize(1),
                () -> assertThat(cart.items().get(0).productId()).isEqualTo(new ProductId("P002")),
                () -> assertThat(cart.items().get(0).title()).isEqualTo(new Title("Test 2")),
                () -> assertThat(cart.items().get(0).unitPrice()).isEqualTo(new Money(10.5f)),
                () -> assertThat(cart.items().get(0).quantity()).isEqualTo(new Quantity(2))
        );
    }

    @Test
    void cart_is_emptied() {
        Cart cart = new CartJdbc(new CartId("C001"), jdbcTemplate);
        cart.empty();
        assertAll(
                () -> assertThat(cart.hasItems()).isFalse(),
                () -> assertThat(cart.items()).isEmpty()
        );
    }
}
