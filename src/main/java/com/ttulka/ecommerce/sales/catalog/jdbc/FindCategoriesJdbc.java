package com.ttulka.ecommerce.sales.catalog.jdbc;

import com.ttulka.ecommerce.sales.catalog.FindCategories;
import com.ttulka.ecommerce.sales.catalog.category.Categories;
import com.ttulka.ecommerce.sales.catalog.category.Category;
import com.ttulka.ecommerce.sales.catalog.category.CategoryId;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation for Find Categories use-cases.
 */
@RequiredArgsConstructor
@Slf4j
final class FindCategoriesJdbc implements FindCategories {

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public Categories all() {
        return new CategoriesJdbc(
                "SELECT id, uri, title FROM categories", jdbcTemplate);
    }

    @Override
    public Category byId(CategoryId id) {
        return new CategoriesJdbc(
                "SELECT id, uri, title FROM categories WHERE id = ?",
                id.value(), jdbcTemplate).stream()
                .findFirst()
                .orElseGet(UnknownCategory::new);
    }
}
