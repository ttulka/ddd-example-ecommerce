package com.ttulka.ecommerce.sales.catalog.jdbc;

import com.ttulka.ecommerce.sales.catalog.FindProducts;
import com.ttulka.ecommerce.sales.catalog.product.Product;
import com.ttulka.ecommerce.sales.catalog.product.ProductId;
import com.ttulka.ecommerce.sales.catalog.product.Products;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation for Find Products use-cases.
 */
@RequiredArgsConstructor
@Slf4j
final class FindProductsJdbc implements FindProducts {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public Products all() {
        return new ProductsJdbc(
                "SELECT id, title, description, price FROM products",
                jdbcTemplate);
    }

    @Override
    public Product byId(ProductId id) {
        return new ProductsJdbc(
                "SELECT id, title, description, price FROM products WHERE id = ?",
                id.value(), jdbcTemplate).stream()
                .findFirst()
                .orElseGet(() -> new UnknownProduct());
    }
}
