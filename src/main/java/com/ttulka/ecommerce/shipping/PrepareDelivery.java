package com.ttulka.ecommerce.shipping;

import java.util.List;

import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.DeliveryItem;
import com.ttulka.ecommerce.shipping.delivery.OrderId;

/**
 * Prepare Delivery use-case.
 */
public interface PrepareDelivery {

    /**
     * Prepares a new delivery.
     *
     * @param orderId the order ID
     * @param items   the delivery items
     * @param address the delivery address
     */
    void prepare(OrderId orderId, List<DeliveryItem> items, Address address);
}
