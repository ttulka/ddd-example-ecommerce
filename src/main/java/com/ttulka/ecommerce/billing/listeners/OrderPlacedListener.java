package com.ttulka.ecommerce.billing.listeners;

import com.ttulka.ecommerce.billing.CollectPayment;
import com.ttulka.ecommerce.billing.payment.Money;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.sales.OrderPlaced;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Listener for OrderPlaced event.
 */
@RequiredArgsConstructor
class OrderPlacedListener {

    private final @NonNull CollectPayment collectPayment;

    @TransactionalEventListener
    @Async
    public void on(OrderPlaced event) {
        collectPayment.collect(
                new ReferenceId(event.orderId),
                new Money(event.orderItems.stream()
                                  .mapToDouble(item -> item.price * item.quantity)
                                  .sum()));
    }
}
