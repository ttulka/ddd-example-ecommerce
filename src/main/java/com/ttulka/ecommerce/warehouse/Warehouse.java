package com.ttulka.ecommerce.warehouse;

/**
 * Warehouse use-cases.
 */
public interface Warehouse {

    /**
     * Returns stock details for a product.
     *
     * @param productId the ID of the product
     * @return the stock details
     */
    InStock leftInStock(ProductId productId);

    /**
     * Puts product items into the stock.
     *
     * @param productId the ID of the product
     * @param amount    the amount of items
     */
    void putIntoStock(ProductId productId, Amount amount);
}
