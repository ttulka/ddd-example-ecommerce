package com.ttulka.ecommerce;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Configuration for sample data.
 * <p>
 * Some sample data to be filled into the database.
 */
@Configuration
@ConditionalOnClass(name = "org.h2.Driver")
@Profile("!test")
class ExampleDataConfig {

    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("/schema.sql", "/example-data.sql")
                .build();
    }
}
