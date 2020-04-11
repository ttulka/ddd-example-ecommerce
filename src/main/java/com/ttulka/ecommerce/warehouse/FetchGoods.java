package com.ttulka.ecommerce.warehouse;

import java.util.Collection;

/**
 * Fetch Goods use-case.
 */
public interface FetchGoods {

    /**
     * Fetches goods for an order.
     *
     * @param orderId the order ID
     * @param toFetch the goods to fetch
     */
    void fetchFromOrder(OrderId orderId, Collection<ToFetch> toFetch);
}
