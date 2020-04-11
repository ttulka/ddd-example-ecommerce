package com.ttulka.ecommerce.sales;

import com.ttulka.ecommerce.sales.category.Categories;
import com.ttulka.ecommerce.sales.category.Category;
import com.ttulka.ecommerce.sales.category.CategoryId;

/**
 * Find Categories use-case.
 */
public interface FindCategories {

    /**
     * Lists all categories.
     *
     * @return all categories
     */
    Categories all();

    /**
     * Finds a category by the category ID.
     *
     * @param id the category ID
     * @return the category
     */
    Category byId(CategoryId id);
}
