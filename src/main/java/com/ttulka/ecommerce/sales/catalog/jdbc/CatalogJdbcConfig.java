package com.ttulka.ecommerce.sales.catalog.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuration for JDBC implementation for Catalog service.
 */
@Configuration
class CatalogJdbcConfig {

    @Bean
    FindCategoriesJdbc findCategoriesJdbc(JdbcTemplate jdbcTemplate) {
        return new FindCategoriesJdbc(jdbcTemplate);
    }

    @Bean
    FindProductsJdbc findProductsJdbc(JdbcTemplate jdbcTemplate) {
        return new FindProductsJdbc(jdbcTemplate);
    }

    @Bean
    FindProductsFromCategoryJdbc findProductsFromCategoryJdbc(JdbcTemplate jdbcTemplate) {
        return new FindProductsFromCategoryJdbc(jdbcTemplate);
    }
}
