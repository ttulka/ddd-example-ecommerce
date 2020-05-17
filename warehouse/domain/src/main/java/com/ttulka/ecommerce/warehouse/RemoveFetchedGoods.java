package com.ttulka.ecommerce.warehouse;

/**
 * Remove Fetched Goods use-case.
 */
public interface RemoveFetchedGoods {

    /**
     * Removes all fetched goods for an order.
     *
     * @param orderId the order ID
     */
    void removeForOrder(OrderId orderId);
}
