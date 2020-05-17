package com.ttulka.ecommerce.sales.catalog.jdbc;

import com.ttulka.ecommerce.sales.catalog.category.Category;
import com.ttulka.ecommerce.sales.catalog.category.CategoryId;
import com.ttulka.ecommerce.sales.catalog.category.Title;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UnknownCategoryTest {

    @Test
    void unknown_category_has_values() {
        Category unknownCategory = new UnknownCategory();
        assertAll(
                () -> assertThat(unknownCategory.id()).isEqualTo(new CategoryId(0)),
                () -> assertThat(unknownCategory.uri()).isNotNull(),
                () -> assertThat(unknownCategory.title()).isNotNull()
        );
    }

    @Test
    void change_title_noop() {
        Category unknownCategory = new UnknownCategory();
        Title unknownTitle = unknownCategory.title();
        unknownCategory.changeTitle(new Title("test"));

        assertThat(unknownCategory.title()).isEqualTo(unknownTitle);
    }
}
