package com.ttulka.ecommerce.shipping.listeners;

import com.ttulka.ecommerce.billing.PaymentCollected;
import com.ttulka.ecommerce.shipping.UpdateDelivery;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.warehouse.GoodsFetched;

import org.springframework.transaction.event.TransactionalEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Listener for GoodsFetched and PaymentCollected events.
 */
@RequiredArgsConstructor
class DispatchingListener {

    private final @NonNull UpdateDelivery updateDelivery;

    @TransactionalEventListener
    public void on(GoodsFetched event) {
        updateDelivery.asFetched(new OrderId(event.orderId));
    }

    @TransactionalEventListener
    public void on(PaymentCollected event) {
        updateDelivery.asPaid(new OrderId(event.referenceId));
    }
}
