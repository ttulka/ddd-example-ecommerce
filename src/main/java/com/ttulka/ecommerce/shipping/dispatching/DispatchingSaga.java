package com.ttulka.ecommerce.shipping.dispatching;

public interface DispatchingSaga {

    void prepared(SagaId sagaId);

    void accepted(SagaId sagaId);

    void fetched(SagaId sagaId);

    void paid(SagaId sagaId);
}
