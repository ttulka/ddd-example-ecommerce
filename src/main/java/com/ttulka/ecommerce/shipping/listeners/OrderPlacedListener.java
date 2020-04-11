package com.ttulka.ecommerce.shipping.listeners;

import java.util.stream.Collectors;

import com.ttulka.ecommerce.sales.OrderPlaced;
import com.ttulka.ecommerce.shipping.PrepareDelivery;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.DeliveryItem;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;
import com.ttulka.ecommerce.shipping.delivery.ProductCode;
import com.ttulka.ecommerce.shipping.delivery.Quantity;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Listener for OrderPlaced event.
 */
@RequiredArgsConstructor
class OrderPlacedListener {

    private final @NonNull PrepareDelivery prepareDelivery;

    @TransactionalEventListener
    @Async
    public void on(OrderPlaced event) {
        prepareDelivery.prepare(
                new OrderId(event.orderId),
                event.orderItems.stream()
                        .map(item -> new DeliveryItem(
                                new ProductCode(item.code),
                                new Quantity(item.quantity)))
                        .collect(Collectors.toList()),
                new Address(
                        new Person(event.customer.name),
                        new Place(event.customer.address)));
    }
}
