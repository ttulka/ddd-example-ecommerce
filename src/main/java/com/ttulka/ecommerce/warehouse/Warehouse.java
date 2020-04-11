package com.ttulka.ecommerce.warehouse;

/**
 * Warehouse use-cases.
 */
public interface Warehouse {

    /**
     * Returns stock details for a product.
     *
     * @param productCode the code of the product
     * @return the stock details
     */
    InStock leftInStock(ProductCode productCode);

    /**
     * Puts product items into the stock.
     *
     * @param productCode the code of the product
     * @param amount      the amount of items
     */
    void putIntoStock(ProductCode productCode, Amount amount);
}
