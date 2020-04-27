package com.ttulka.ecommerce.sales.catalog.product;

import java.util.stream.Stream;

/**
 * Products collection.
 */
public interface Products {

    Products range(int start, int limit);

    Products range(int limit);

    Stream<Product> stream();
}
