package com.ttulka.ecommerce.shipping;

import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.OrderId;

/**
 * Prepare Delivery use-case.
 */
public interface PrepareDelivery {

    /**
     * Prepares a new delivery.
     *
     * @param orderId the order ID
     * @param address the delivery address
     * @throws {@link Delivery.DeliveryAlreadyPreparedException} when already prepared.
     */
    void prepare(OrderId orderId, Address address);
}
