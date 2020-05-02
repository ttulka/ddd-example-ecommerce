package com.ttulka.ecommerce.shipping.dispatching;

/**
 * Dispatching Saga. Integration service for dispatching Delivery.
 * <p>
 * A saga is a long-running process with a state.
 * <p>
 * A saga handles multiple messages and holds its private state.
 */
public interface DispatchingSaga {

    void prepared(SagaId sagaId);

    void accepted(SagaId sagaId);

    void fetched(SagaId sagaId);

    void paid(SagaId sagaId);
}
