package com.ttulka.ecommerce.shipping.dispatching.listeners;

import com.ttulka.ecommerce.billing.payment.PaymentCollected;
import com.ttulka.ecommerce.sales.order.OrderPlaced;
import com.ttulka.ecommerce.shipping.delivery.DeliveryPrepared;
import com.ttulka.ecommerce.shipping.dispatching.DispatchingSaga;
import com.ttulka.ecommerce.shipping.dispatching.SagaId;
import com.ttulka.ecommerce.warehouse.GoodsFetched;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class DispatchingListeners {

    private final @NonNull DispatchingSaga saga;

    @TransactionalEventListener
    @Async
    public void on(DeliveryPrepared event) {
        saga.prepared(new SagaId(event.orderId));
    }

    @TransactionalEventListener
    @Async
    public void on(OrderPlaced event) {
        saga.accepted(new SagaId(event.orderId));
    }

    @TransactionalEventListener
    @Async
    public void on(GoodsFetched event) {
        saga.fetched(new SagaId(event.orderId));
    }

    @TransactionalEventListener
    @Async
    public void on(PaymentCollected event) {
        saga.paid(new SagaId(event.referenceId));
    }
}
