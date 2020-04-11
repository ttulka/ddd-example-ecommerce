package com.ttulka.ecommerce.sales.product.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.sales.FindProducts;
import com.ttulka.ecommerce.sales.product.Product;
import com.ttulka.ecommerce.sales.product.ProductId;
import com.ttulka.ecommerce.sales.product.Products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@ContextConfiguration(classes = ProductJdbcConfig.class)
@Sql(statements = "INSERT INTO products VALUES " +
        "('000', 'code0', 'A', 'desc0', 1.0), " +
        "('001', 'code1', 'B', 'desc1', 2.0), " +
        "('002', 'code2', 'C', 'desc2', 3.0)")
class ProductsTest {

    @Autowired
    private FindProducts findProducts;

    @Test
    void products_are_streamed() {
        Products products = findProducts.all();
        List<Product> list = products.stream().collect(Collectors.toList());
        assertAll(
                () -> assertThat(list.size()).isEqualTo(3),
                () -> assertThat(list.get(0).id()).isEqualTo(new ProductId("000")),
                () -> assertThat(list.get(1).id()).isEqualTo(new ProductId("001")),
                () -> assertThat(list.get(2).id()).isEqualTo(new ProductId("002"))
        );
    }

    @Test
    void products_are_limited() {
        Products products = findProducts.all()
                .range(2, 1);
        List<Product> list = products.stream().collect(Collectors.toList());
        assertAll(
                () -> assertThat(list.size()).isEqualTo(1),
                () -> assertThat(list.get(0).id()).isEqualTo(new ProductId("002"))
        );
    }

    @Test
    void limited_start_is_greater_than_zero() {
        Products products = findProducts.all();
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> products.range(-1, 1)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> products.range(1).range(-1, 1))
        );
    }

    @Test
    void limited_limit_is_greater_than_zero() {
        Products products = findProducts.all();
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> products.range(0)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> products.range(-1)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> products.range(1).range(0)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> products.range(1).range(-1))
        );
    }
}
