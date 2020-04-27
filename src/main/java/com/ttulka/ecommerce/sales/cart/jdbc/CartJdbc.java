package com.ttulka.ecommerce.sales.cart.jdbc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.cart.Cart;
import com.ttulka.ecommerce.sales.cart.CartId;
import com.ttulka.ecommerce.sales.cart.item.CartItem;
import com.ttulka.ecommerce.sales.cart.item.ProductId;
import com.ttulka.ecommerce.sales.cart.item.Title;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
final class CartJdbc implements Cart {

    private final @NonNull CartId id;

    private final @NonNull JdbcTemplate jdbcTemplate;

    private List<CartItem> items;

    @Override
    public CartId id() {
        return id;
    }

    @Override
    public List<CartItem> items() {
        if (items == null) {
            items = jdbcTemplate.queryForList(
                    "SELECT product_id, title, price, quantity FROM cart_items " +
                    "WHERE cart_id = ?", id.value())
                    .stream()
                    .map(this::toCartItem)
                    .collect(Collectors.toList());
        }
        return items;
    }

    private CartItem toCartItem(Map<String, Object> entry) {
        return new CartItem(
                new ProductId(entry.get("product_id")),
                new Title((String) entry.get("title")),
                new Money(((BigDecimal) entry.get("price")).floatValue()),
                new Quantity((Integer) entry.get("quantity")));
    }

    @Override
    public boolean hasItems() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(cart_id) FROM cart_items " +
                "WHERE cart_id = ?", Integer.class, id.value()) > 0;
    }

    @Override
    public void add(CartItem toAdd) {
        if (hasItem(toAdd)) {
            jdbcTemplate.update(
                    "UPDATE cart_items SET quantity = quantity + ? " +
                    "WHERE cart_id = ? AND product_id = ? AND title = ? AND price = ?",
                    toAdd.quantity().value(), id.value(),
                    toAdd.productId().value(), toAdd.title().value(), toAdd.unitPrice().value());
        } else {
            jdbcTemplate.update(
                    "INSERT INTO cart_items VALUES (?, ?, ?, ?, ?)",
                    toAdd.productId().value(), toAdd.title().value(), toAdd.unitPrice().value(),
                    toAdd.quantity().value(), id.value());
        }
    }

    private boolean hasItem(CartItem item) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(product_id) FROM cart_items " +
                "WHERE product_id = ? AND title = ? AND price = ?", Integer.class,
                item.productId().value(), item.title().value(), item.unitPrice().value()) > 0;
    }

    @Override
    public void remove(ProductId productId) {
        jdbcTemplate.update(
                "DELETE FROM cart_items WHERE product_id = ? AND cart_id = ?",
                productId.value(), id.value());
    }

    @Override
    public void empty() {
        jdbcTemplate.update("DELETE FROM cart_items WHERE cart_id = ?", id.value());
    }
}
