package com.ttulka.ecommerce.sales.category.jdbc;

import com.ttulka.ecommerce.sales.category.Category;
import com.ttulka.ecommerce.sales.category.CategoryId;
import com.ttulka.ecommerce.sales.category.Title;
import com.ttulka.ecommerce.sales.category.Uri;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * JDBC implementation for Category entity.
 */
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "uri", "title"})
@AllArgsConstructor
final class CategoryJdbc implements Category {

    private final @NonNull CategoryId id;
    private final @NonNull Uri uri;
    private @NonNull Title title;

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public CategoryId id() {
        return id;
    }

    @Override
    public Uri uri() {
        return uri;
    }

    @Override
    public Title title() {
        return title;
    }

    @Override
    public void changeTitle(Title title) {
        this.title = title;
        jdbcTemplate.update("UPDATE categories SET title = ? WHERE id = ?",
                            title.value(), id.value());
    }
}
