package com.ttulka.ecommerce.sales.product;

import com.ttulka.ecommerce.common.money.Money;
import com.ttulka.ecommerce.sales.category.CategoryId;

/**
 * Product entity.
 */
public interface Product {

    ProductId id();

    Code code();

    Title title();

    Description description();

    Money price();

    void changeTitle(Title title);

    void changeDescription(Description description);

    void changePrice(Money price);

    void putForSale();

    void categorize(CategoryId categoryId);
}
