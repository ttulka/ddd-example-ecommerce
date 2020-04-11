package com.ttulka.ecommerce.catalogue;

import java.util.List;

import com.ttulka.ecommerce.common.events.EventPublisher;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = {CatalogueConfig.class, CatalogueTest.ServicesTestConfig.class})
@Sql("/test-data-catalogue.sql")
class CatalogueTest {

    @Autowired
    private Catalogue catalogue;

    @Test
    void all_products_are_returned() {
        List<Catalogue.CatalogueProductData> products = catalogue.allProducts();
        assertAll(
                () -> Assertions.assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).code).isEqualTo("001"),
                () -> assertThat(products.get(0).title).isEqualTo("Prod 1"),
                () -> assertThat(products.get(0).description).isEqualTo("Prod 1 Desc"),
                () -> assertThat(products.get(0).price).isEqualTo(1.f),
                () -> assertThat(products.get(1).code).isEqualTo("002"),
                () -> assertThat(products.get(1).title).isEqualTo("Prod 2"),
                () -> assertThat(products.get(1).description).isEqualTo("Prod 2 Desc"),
                () -> assertThat(products.get(1).price).isEqualTo(2.f)
        );
    }

    @Test
    void products_from_a_category_are_returned() {
        List<Catalogue.CatalogueProductData> products = catalogue.productsInCategory("cat1");
        assertAll(
                () -> Assertions.assertThat(products).hasSize(1),
                () -> assertThat(products.get(0).code).isEqualTo("001"),
                () -> assertThat(products.get(0).title).isEqualTo("Prod 1"),
                () -> assertThat(products.get(0).description).isEqualTo("Prod 1 Desc"),
                () -> assertThat(products.get(0).price).isEqualTo(1.f)
        );
    }

    @Test
    void categories_are_returned() {
        List<Catalogue.CatalogueCategoryData> categories = catalogue.categories();
        assertAll(
                () -> Assertions.assertThat(categories).hasSize(2),
                () -> assertThat(categories.get(0).uri).isEqualTo("cat1"),
                () -> assertThat(categories.get(0).title).isEqualTo("Cat 1"),
                () -> assertThat(categories.get(1).uri).isEqualTo("cat2"),
                () -> assertThat(categories.get(1).title).isEqualTo("Cat 2")
        );
    }

    @Configuration
    @ComponentScan({
            "com.ttulka.ecommerce.sales",
            "com.ttulka.ecommerce.warehouse"})
    static class ServicesTestConfig {
        @MockBean
        private EventPublisher eventPublisher;
    }
}
