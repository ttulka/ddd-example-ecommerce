package com.ttulka.ecommerce.shipping.listeners;

import com.ttulka.ecommerce.common.events.EventPublisher;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Resending an event in a new transaction.
 */
@RequiredArgsConstructor
class ResendEvent {

    private final @NonNull EventPublisher eventPublisher;

    /**
     * Resends an event in a new transaction.
     *
     * @param event the event
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resend(Object event) {
        eventPublisher.raise(event);
    }
}
