package com.ttulka.ecommerce.sales.category;

/**
 * Category entity.
 */
public interface Category {

    CategoryId id();

    Uri uri();

    Title title();

    void changeTitle(Title title);
}
