package com.ttulka.ecommerce.billing.payment;

import java.util.stream.Stream;

/**
 * Payments collection.
 */
public interface Payments {

    Payments range(int start, int limit);

    Payments range(int limit);

    Stream<Payment> stream();
}
