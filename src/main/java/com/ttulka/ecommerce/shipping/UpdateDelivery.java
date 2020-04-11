package com.ttulka.ecommerce.shipping;

import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.OrderId;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Update Delivery use-case.
 */
@RequiredArgsConstructor
public class UpdateDelivery {

    private final @NonNull FindDeliveries findDeliveries;

    /**
     * Updates a delivery by the order ID as fetched.
     *
     * @param orderId the order ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void asFetched(OrderId orderId) {
        Delivery delivery = findDeliveries.byOrderId(orderId);
        delivery.markAsFetched();

        if (delivery.isReadyToDispatch()) {
            delivery.dispatch();
        }
    }

    /**
     * Updates a delivery by the order ID as paid.
     *
     * @param orderId the order ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void asPaid(OrderId orderId) {
        Delivery delivery = findDeliveries.byOrderId(orderId);
        delivery.markAsPaid();

        if (delivery.isReadyToDispatch()) {
            delivery.dispatch();
        }
    }
}
