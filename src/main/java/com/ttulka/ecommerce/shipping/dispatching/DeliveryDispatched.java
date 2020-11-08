package com.ttulka.ecommerce.shipping.dispatching;

import java.time.Instant;

import com.ttulka.ecommerce.common.events.DomainEvent;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Delivery Dispatched domain event.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = "orderId")
@ToString
public final class DeliveryDispatched implements DomainEvent {

    public final @NonNull Instant when;
    public final @NonNull String orderId;
}
