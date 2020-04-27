package com.ttulka.ecommerce.sales.catalog.jdbc;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.sales.catalog.FindProducts;
import com.ttulka.ecommerce.sales.catalog.product.Description;
import com.ttulka.ecommerce.sales.catalog.product.Product;
import com.ttulka.ecommerce.sales.catalog.product.ProductId;
import com.ttulka.ecommerce.sales.catalog.product.Title;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = CatalogJdbcConfig.class)
@Sql(statements = "INSERT INTO products VALUES ('TEST1', 'Test', 'Test', 1.00);")
class ChangeProductTest {

    @Autowired
    private FindProducts findProducts;

    @Test
    void product_title_is_changed() {
        Product product = findProducts.byId(new ProductId("TEST1"));
        product.changeTitle(new Title("Updated title"));

        Product updated = findProducts.byId(new ProductId("TEST1"));

        assertThat(updated.title()).isEqualTo(new Title("Updated title"));
    }

    @Test
    void product_description_is_changed() {
        Product product = findProducts.byId(new ProductId("TEST1"));
        product.changeDescription(new Description("Updated description"));

        Product updated = findProducts.byId(new ProductId("TEST1"));

        assertThat(updated.description()).isEqualTo(new Description("Updated description"));
    }

    @Test
    void product_price_is_changed() {
        Product product = findProducts.byId(new ProductId("TEST1"));
        product.changePrice(new Money(100.5f));

        Product updated = findProducts.byId(new ProductId("TEST1"));

        assertThat(updated.price()).isEqualTo(new Money(100.5f));
    }
}
