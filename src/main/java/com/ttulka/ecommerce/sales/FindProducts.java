package com.ttulka.ecommerce.sales;

import com.ttulka.ecommerce.sales.category.Uri;
import com.ttulka.ecommerce.sales.product.Code;
import com.ttulka.ecommerce.sales.product.Product;
import com.ttulka.ecommerce.sales.product.Products;

/**
 * Find Products use-case.
 */
public interface FindProducts {

    /**
     * Lists all products.
     *
     * @return all products
     */
    Products all();

    /**
     * Lists products from the category
     *
     * @param categoryUri the URI of the category
     * @return the products from the category
     */
    Products fromCategory(Uri categoryUri);

    /**
     * Finds a product by the code.
     *
     * @param code the code
     * @return the product
     */
    Product byCode(Code code);
}
