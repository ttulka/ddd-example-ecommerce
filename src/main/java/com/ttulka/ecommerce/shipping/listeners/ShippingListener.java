package com.ttulka.ecommerce.shipping.listeners;

import java.util.Timer;
import java.util.TimerTask;

import com.ttulka.ecommerce.billing.PaymentCollected;
import com.ttulka.ecommerce.sales.OrderPlaced;
import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.UpdateDelivery;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.warehouse.GoodsFetched;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Listener for GoodsFetched and PaymentCollected events.
 * <p>
 * It is possible that those events come before the Delivery is prepared. In such a case the events must be rejected and resent.
 * <p>
 * The current implementation re-send the unordered events with a delay for later processing.
 */
@RequiredArgsConstructor
class ShippingListener {

    private final @NonNull UpdateDelivery updateDelivery;
    private final @NonNull FindDeliveries findDeliveries;

    @TransactionalEventListener
    @Async
    public void on(OrderPlaced event) {
        var orderId = new OrderId(event.orderId);
        if (findDeliveries.isPrepared(orderId)) {
            // when needed, here could be set delivery items from the order etc...
            updateDelivery.asAccepted(orderId);
        } else {
            resendWithDelay(event);   // event came in wrong order
        }
    }

    @TransactionalEventListener
    @Async
    public void on(GoodsFetched event) {
        var orderId = new OrderId(event.orderId);
        if (findDeliveries.isPrepared(orderId)) {
            updateDelivery.asFetched(orderId);
        } else {
            resendWithDelay(event);   // event came in wrong order
        }
    }

    @TransactionalEventListener
    @Async
    public void on(PaymentCollected event) {
        var orderId = new OrderId(event.referenceId);
        if (findDeliveries.isPrepared(orderId)) {
            updateDelivery.asPaid(orderId);
        } else {
            resendWithDelay(event);   // event came in wrong order
        }
    }

    // Simple resend implementation.
    // A real implementation should reject the event which will be resent later again by message bus.

    private void resendWithDelay(OrderPlaced event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ShippingListener.this.on(event);
            }
        }, 100);
    }

    private void resendWithDelay(GoodsFetched event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ShippingListener.this.on(event);
            }
        }, 100);
    }

    private void resendWithDelay(PaymentCollected event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ShippingListener.this.on(event);
            }
        }, 100);
    }
}
