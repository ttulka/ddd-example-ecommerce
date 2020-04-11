package com.ttulka.ecommerce.shipping;

import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfos;
import com.ttulka.ecommerce.shipping.delivery.OrderId;

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
     * Finds a delivery by the order ID.
     *
     * @param orderId the order ID
     * @return the delivery
     */
    Delivery byOrderId(OrderId orderId);
}
