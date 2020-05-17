package com.ttulka.ecommerce.warehouse;

import java.time.Instant;

import com.ttulka.ecommerce.common.events.DomainEvent;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Goods Fetched domain event.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = "orderId")
@ToString
public final class GoodsFetched implements DomainEvent {

    public final @NonNull Instant when;
    public final @NonNull String orderId;
}
