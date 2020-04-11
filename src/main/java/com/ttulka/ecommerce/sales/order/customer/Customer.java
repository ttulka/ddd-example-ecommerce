package com.ttulka.ecommerce.sales.order.customer;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Customer entity.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public final class Customer {

    private final @NonNull Name name;
    private final @NonNull Address address;

    public Name name() {
        return name;
    }

    public Address address() {
        return address;
    }
}
