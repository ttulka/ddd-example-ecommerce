package com.ttulka.ecommerce.sales.order.customer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    @Test
    void customer_is_created() {
        Customer customer = new Customer(
                new Name("test name"),
                new Address("test address 123")
        );
        assertThat(customer).isNotNull();
    }
}
