package com.ttulka.ecommerce.sales.product.jdbc;

import com.ttulka.ecommerce.sales.FindProducts;
import com.ttulka.ecommerce.sales.category.Uri;
import com.ttulka.ecommerce.sales.product.Code;
import com.ttulka.ecommerce.sales.product.Product;
import com.ttulka.ecommerce.sales.product.Products;
import com.ttulka.ecommerce.sales.product.UnknownProduct;

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
                "SELECT id, code, title, description, price FROM products",
                jdbcTemplate);
    }

    @Override
    public Products fromCategory(@NonNull Uri categoryUri) {
        return new ProductsJdbc(
                "SELECT p.id, p.code, p.title, p.description, p.price FROM products AS p " +
                "JOIN products_in_categories AS pc ON pc.product_id = p.id " +
                "JOIN categories AS c ON c.id = pc.category_id " +
                "WHERE c.uri = ?",
                categoryUri.value(), jdbcTemplate);
    }

    @Override
    public Product byCode(@NonNull Code code) {
        return new ProductsJdbc(
                "SELECT id, code, title, description, price FROM products WHERE code = ?",
                code.value(), jdbcTemplate).stream()
                .findFirst()
                .orElseGet(() -> new UnknownProduct());
    }
}
