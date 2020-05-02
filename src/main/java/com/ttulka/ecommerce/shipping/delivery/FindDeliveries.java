package com.ttulka.ecommerce.shipping.delivery;

/**
 * Find Deliveries use-case.
 */
public interface FindDeliveries {

    /**
     * Finds all deliveries.
     *
     * @return all deliveries
     */
    DeliveryInfos all();

    /**
     * Finds a delivery by the Order ID.
     *
     * @param orderId the order ID
     * @return the delivery
     */
    Delivery byOrder(OrderId orderId);

    /**
     * Checks if a delivery is prepared for the order ID.
     *
     * @param orderId the order ID
     * @return true if a delivery is prepared, otherwise false
     */
    boolean isPrepared(OrderId orderId);
}
