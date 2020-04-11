package com.ttulka.ecommerce.sales.product;

import com.ttulka.ecommerce.sales.category.CategoryId;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UnknownProductTest {

    @Test
    void unknown_product_has_values() {
        Product unknownProduct = new UnknownProduct();
        assertAll(
                () -> assertThat(unknownProduct.id()).isEqualTo(new ProductId(0)),
                () -> assertThat(unknownProduct.code()).isNotNull(),
                () -> assertThat(unknownProduct.title()).isNotNull(),
                () -> assertThat(unknownProduct.description()).isNotNull(),
                () -> assertThat(unknownProduct.price()).isNotNull()
        );
    }

    @Test
    void unknown_product_has_a_zero_price() {
        Product unknownProduct = new UnknownProduct();
        assertThat(unknownProduct.price()).isEqualTo(new Price(0.f));
    }

    @Test
    void change_title_noop() {
        Product unknownProduct = new UnknownProduct();
        Title unknownTitle = unknownProduct.title();
        unknownProduct.changeTitle(new Title("test"));

        assertThat(unknownProduct.title()).isEqualTo(unknownTitle);
    }

    @Test
    void change_description_noop() {
        Product unknownProduct = new UnknownProduct();
        Description unknownDescription = unknownProduct.description();
        unknownProduct.changeDescription(new Description("test"));

        assertThat(unknownProduct.description()).isEqualTo(unknownDescription);
    }

    @Test
    void change_price_noop() {
        Product unknownProduct = new UnknownProduct();
        Price unknownPrice = unknownProduct.price();
        unknownProduct.changePrice(new Price(1.f));

        assertThat(unknownProduct.price()).isEqualTo(unknownPrice);
    }

    @Test
    void put_for_sale_noop() {
        Product unknownProduct = new UnknownProduct();
        unknownProduct.putForSale();
    }

    @Test
    void categorize_noop() {
        Product unknownProduct = new UnknownProduct();
        unknownProduct.categorize(new CategoryId(123));
    }
}
