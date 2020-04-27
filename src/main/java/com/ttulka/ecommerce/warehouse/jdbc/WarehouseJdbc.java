package com.ttulka.ecommerce.warehouse.jdbc;

import com.ttulka.ecommerce.warehouse.Amount;
import com.ttulka.ecommerce.warehouse.InStock;
import com.ttulka.ecommerce.warehouse.ProductId;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * JDBC implementation for Warehouse use-cases.
 */
@RequiredArgsConstructor
final class WarehouseJdbc implements Warehouse {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public InStock leftInStock(ProductId productId) {
        return jdbcTemplate.queryForList(
                "SELECT amount FROM products_in_stock WHERE product_id = ?",
                Integer.class, productId.value())
                .stream().findAny()
                .map(InStock::new)
                .orElseGet(() -> new InStock(0));
    }

    @Override
    public void putIntoStock(ProductId productId, Amount amount) {
        jdbcTemplate.update(
                "INSERT INTO products_in_stock VALUES (?, ?)",
                productId.value(), amount.value());
    }
}
