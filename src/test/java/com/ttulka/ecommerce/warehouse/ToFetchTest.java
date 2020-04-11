package com.ttulka.ecommerce.warehouse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ToFetchTest {

    @Test
    void to_fetch_values() {
        ToFetch toFetch = new ToFetch(new ProductCode("test"), new Amount(123));
        assertAll(
                () -> assertThat(toFetch.productCode()).isEqualTo(new ProductCode("test")),
                () -> assertThat(toFetch.amount()).isEqualTo(new Amount(123))
        );
    }
}
