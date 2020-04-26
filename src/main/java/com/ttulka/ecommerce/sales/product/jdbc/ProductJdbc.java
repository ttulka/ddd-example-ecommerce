package com.ttulka.ecommerce.sales.product.jdbc;

import com.ttulka.ecommerce.common.money.Money;
import com.ttulka.ecommerce.sales.category.CategoryId;
import com.ttulka.ecommerce.sales.product.Code;
import com.ttulka.ecommerce.sales.product.Description;
import com.ttulka.ecommerce.sales.product.Product;
import com.ttulka.ecommerce.sales.product.ProductId;
import com.ttulka.ecommerce.sales.product.Title;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * JDBC implementation for Product entity.
 */
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "code", "title", "price"})
final class ProductJdbc implements Product {

    private final @NonNull ProductId id;
    private final @NonNull Code code;

    private @NonNull Title title;
    private @NonNull Description description;
    private @NonNull Money price;

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public ProductId id() {
        return id;
    }

    @Override
    public Code code() {
        return code;
    }

    @Override
    public Title title() {
        return title;
    }

    @Override
    public Description description() {
        return description;
    }

    @Override
    public Money price() {
        return price;
    }

    @Override
    public void changeTitle(Title title) {
        this.title = title;
        jdbcTemplate.update("UPDATE products SET title = ? WHERE id = ?",
                            title.value(), id.value());
    }

    @Override
    public void changeDescription(Description description) {
        this.description = description;
        jdbcTemplate.update("UPDATE products SET description = ? WHERE id = ?",
                            description.value(), id.value());
    }

    @Override
    public void changePrice(Money price) {
        this.price = price;
        jdbcTemplate.update("UPDATE products SET price = ? WHERE id = ?",
                            price.value(), id.value());
    }

    @Override
    public void putForSale() {
        jdbcTemplate.update("INSERT INTO products VALUES(?, ?, ?, ?, ?)",
                            id.value(), code.value(), title.value(), description.value(), price.value());
    }

    @Override
    public void categorize(CategoryId categoryId) {
        jdbcTemplate.update("INSERT INTO products_in_categories VALUES(?, ?)",
                            id.value(), categoryId.value());
    }
}
