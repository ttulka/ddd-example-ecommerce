package com.ttulka.ecommerce.shipping.delivery;

/**
 * Delivery entity.
 */
public interface Delivery {

    DeliveryId id();

    OrderId orderId();

    Address address();

    /**
     * @throws {@link DeliveryAlreadyPreparedException} when already prepared.
     */
    void prepare();

    void markAsAccepted();

    void markAsFetched();

    void markAsPaid();

    /**
     * @throws {@link DeliveryNotReadyToBeDispatchedException} when not ready to be dispatched.
     * @throws {@link DeliveryAlreadyDispatchedException} when already dispatched.
     */
    void dispatch();

    boolean isDispatched();

    boolean isReadyToDispatch();

    final class DeliveryAlreadyPreparedException extends IllegalStateException {
    }

    final class DeliveryNotReadyToBeDispatchedException extends IllegalStateException {
    }

    final class DeliveryAlreadyDispatchedException extends IllegalStateException {
    }
}
