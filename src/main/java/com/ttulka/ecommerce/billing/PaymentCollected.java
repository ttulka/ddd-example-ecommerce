package com.ttulka.ecommerce.billing;

import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Payment Collected domain event.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = "referenceId")
@ToString
public final class PaymentCollected {

    public final @NonNull Instant when;
    public final @NonNull String referenceId;
}
