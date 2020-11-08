package com.ttulka.ecommerce.shipping.delivery;

/**
 * Dispatch Delivery use-case.
 */
public interface DispatchDelivery {

    /**
     * Dispatches a delivery by the order ID.
     *
     * @param orderId the order ID
     */
    void byOrder(OrderId orderId);
}
