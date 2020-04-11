package com.ttulka.ecommerce.warehouse.jdbc;

import com.ttulka.ecommerce.warehouse.Amount;
import com.ttulka.ecommerce.warehouse.InStock;
import com.ttulka.ecommerce.warehouse.ProductCode;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.springframework.dao.DataAccessException;
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
    public InStock leftInStock(ProductCode productCode) {
        try {
            Integer leftInStock = jdbcTemplate.queryForObject(
                    "SELECT amount FROM products_in_stock " +
                    "WHERE product_code = ?", Integer.class, productCode.value());
            if (leftInStock != null) {
                return new InStock(leftInStock);
            }
        } catch (DataAccessException ignore) {
        }
        return new InStock(0);
    }

    @Override
    public void putIntoStock(ProductCode productCode, Amount amount) {
        jdbcTemplate.update(
                "INSERT INTO products_in_stock VALUES (?, ?)",
                productCode.value(), amount.value());
    }
}
