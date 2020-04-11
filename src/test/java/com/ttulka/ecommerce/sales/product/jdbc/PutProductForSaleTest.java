package com.ttulka.ecommerce.sales.product.jdbc;

import java.util.UUID;

import com.ttulka.ecommerce.sales.FindProducts;
import com.ttulka.ecommerce.sales.product.Code;
import com.ttulka.ecommerce.sales.product.Description;
import com.ttulka.ecommerce.sales.product.Price;
import com.ttulka.ecommerce.sales.product.Product;
import com.ttulka.ecommerce.sales.product.ProductId;
import com.ttulka.ecommerce.sales.product.Title;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = ProductJdbcConfig.class)
class PutProductForSaleTest {

    @Autowired
    private FindProducts findProducts;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void product_put_for_sale_is_found() {
        String code = UUID.randomUUID().toString();
        Product product = new ProductJdbc(
                new ProductId(123),
                new Code(code),
                new Title("test"),
                new Description("test"),
                new Price(1.f),
                jdbcTemplate
        );
        product.putForSale();

        Product productFound = findProducts.byCode(new Code(code));

        assertThat(productFound.id()).isEqualTo(new ProductId(123));
    }
}
