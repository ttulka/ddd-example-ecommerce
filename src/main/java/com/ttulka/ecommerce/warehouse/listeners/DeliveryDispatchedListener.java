package com.ttulka.ecommerce.warehouse.listeners;

import com.ttulka.ecommerce.shipping.DeliveryDispatched;
import com.ttulka.ecommerce.warehouse.OrderId;
import com.ttulka.ecommerce.warehouse.RemoveFetchedGoods;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Listener for DeliveryDispatched event.
 */
@RequiredArgsConstructor
class DeliveryDispatchedListener {

    private final @NonNull RemoveFetchedGoods removeFetchedGoods;

    @TransactionalEventListener
    @Async
    public void on(DeliveryDispatched event) {
        removeFetchedGoods.removeForOrder(new OrderId(event.orderId));
    }
}
