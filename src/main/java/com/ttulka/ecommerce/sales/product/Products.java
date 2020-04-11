package com.ttulka.ecommerce.sales.product;

import java.util.stream.Stream;

/**
 * Products collection.
 */
public interface Products {

    Products range(int start, int limit);

    Products range(int limit);

    Stream<Product> stream();
}
