package com.ttulka.ecommerce.sales.product;

import com.ttulka.ecommerce.sales.category.CategoryId;

/**
 * Product entity.
 */
public interface Product {

    ProductId id();

    Code code();

    Title title();

    Description description();

    Price price();

    void changeTitle(Title title);

    void changeDescription(Description description);

    void changePrice(Price price);

    void putForSale();

    void categorize(CategoryId categoryId);
}
