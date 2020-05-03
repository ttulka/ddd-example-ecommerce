package com.ttulka.ecommerce.shipping.dispatching;

/**
 * Dispatching Saga. Integration service for dispatching Delivery.
 * <p>
 * A saga is a long-running process with a state.
 * <p>
 * A saga handles multiple messages and holds its private state.
 */
public interface DispatchingSaga {

    void prepared(OrderId orderId);

    void accepted(OrderId orderId);

    void fetched(OrderId orderId);

    void paid(OrderId orderId);
}
