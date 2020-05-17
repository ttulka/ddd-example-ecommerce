package com.ttulka.ecommerce.common.events;

/**
 * Publisher for domain events.
 * <p>
 * Decouples the domain layer from the messaging implementation.
 */
public interface EventPublisher {

    /**
     * Raises a domain event.
     *
     * @param event the domain event.
     */
    void raise(DomainEvent event);
}
