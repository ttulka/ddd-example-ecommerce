package com.ttulka.ecommerce.sales.catalog.jdbc;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.sales.catalog.category.CategoryId;
import com.ttulka.ecommerce.sales.catalog.product.Description;
import com.ttulka.ecommerce.sales.catalog.product.Product;
import com.ttulka.ecommerce.sales.catalog.product.ProductId;
import com.ttulka.ecommerce.sales.catalog.product.Title;

import lombok.ToString;

/**
 * Null object implementation for Product entity.
 */
@ToString
final class UnknownProduct implements Product {

    @Override
    public ProductId id() {
        return new ProductId(0);
    }

    @Override
    public Title title() {
        return new Title("unknown product");
    }

    @Override
    public Description description() {
        return new Description("This product is not to be found.");
    }

    @Override
    public Money price() {
        return new Money(0.0f);
    }

    @Override
    public void changeTitle(Title title) {
        // do nothing
    }

    @Override
    public void changeDescription(Description description) {
        // do nothing
    }

    @Override
    public void changePrice(Money price) {
        // do nothing
    }

    @Override
    public void putForSale() {
        // do nothing
    }

    @Override
    public void categorize(CategoryId categoryId) {
        // do nothing
    }
}
