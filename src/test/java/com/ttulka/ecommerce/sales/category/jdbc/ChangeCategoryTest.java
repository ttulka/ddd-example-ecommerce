package com.ttulka.ecommerce.sales.category.jdbc;

import com.ttulka.ecommerce.sales.FindCategories;
import com.ttulka.ecommerce.sales.category.Category;
import com.ttulka.ecommerce.sales.category.CategoryId;
import com.ttulka.ecommerce.sales.category.Title;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = CategoryJdbcConfig.class)
@Sql(statements = "INSERT INTO categories VALUES ('1', 'test', 'Test');")
class ChangeCategoryTest {

    @Autowired
    private FindCategories findCategories;

    @Test
    void product_title_is_changed() {
        Category category = findCategories.byId(new CategoryId(1));
        category.changeTitle(new Title("Updated title"));

        Category productUpdated = findCategories.byId(new CategoryId(1));

        assertThat(productUpdated.title()).isEqualTo(new Title("Updated title"));
    }
}
