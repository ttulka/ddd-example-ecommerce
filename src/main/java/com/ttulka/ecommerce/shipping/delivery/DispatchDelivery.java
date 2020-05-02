package com.ttulka.ecommerce.shipping.delivery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Dispatch Delivery use-case.
 */
@RequiredArgsConstructor
public class DispatchDelivery {

    private final @NonNull FindDeliveries findDeliveries;

    /**
     * Dispatches a delivery by the order ID.
     *
     * @param orderId the order ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void byOrder(OrderId orderId) {
        Delivery delivery = findDeliveries.byOrder(orderId);
        delivery.dispatch();
    }
}
