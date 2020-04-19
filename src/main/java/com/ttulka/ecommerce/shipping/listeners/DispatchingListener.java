package com.ttulka.ecommerce.shipping.listeners;

import java.util.Timer;
import java.util.TimerTask;

import com.ttulka.ecommerce.billing.PaymentCollected;
import com.ttulka.ecommerce.common.events.DomainEvent;
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
 * It is possible that those events come before the Delivery is prepared. In such a case the events must not be accepted.
 * <p>
 * The current implementation re-send the unordered events with a delay for later processing.
 */
@RequiredArgsConstructor
class DispatchingListener {

    private final @NonNull UpdateDelivery updateDelivery;
    private final @NonNull FindDeliveries findDeliveries;

    private final @NonNull ResendEvent resendEvent;

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

    private void resendWithDelay(DomainEvent event) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                resendEvent.resend(event);
            }
        }, 100);
    }
}
