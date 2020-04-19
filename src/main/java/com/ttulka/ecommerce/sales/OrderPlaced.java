package com.ttulka.ecommerce.sales;

import java.time.Instant;
import java.util.List;

import com.ttulka.ecommerce.common.events.DomainEvent;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

/**
 * Order Placed domain event.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = "orderId")
@ToString
public final class OrderPlaced implements DomainEvent {

    public final @NonNull Instant when;
    public final @NonNull String orderId;
    public final @NonNull List<OrderItemData> orderItems;
    public final @NonNull CustomerData customer;

    @Value
    public static final class OrderItemData {

        public final String code;
        public final String title;
        public final Float price;
        public final Integer quantity;
    }

    @Value
    public static final class CustomerData {

        public final String name;
        public final String address;
    }
}
