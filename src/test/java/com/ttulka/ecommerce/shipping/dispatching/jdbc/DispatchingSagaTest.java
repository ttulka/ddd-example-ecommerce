package com.ttulka.ecommerce.shipping.dispatching.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.delivery.DispatchDelivery;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.dispatching.DispatchingSaga;
import com.ttulka.ecommerce.shipping.dispatching.SagaId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@JdbcTest
@ContextConfiguration(classes = DispatchingSagaJdbcConfig.class)
class DispatchingSagaTest {

    @Autowired
    private DispatchingSaga saga;

    @MockBean
    private DispatchDelivery dispatchDelivery;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void delivery_is_dispatched() {
        saga.prepared(new SagaId("TEST"));
        saga.accepted(new SagaId("TEST"));
        saga.fetched(new SagaId("TEST"));
        saga.paid(new SagaId("TEST"));

        verify(dispatchDelivery).byOrder(new OrderId("TEST"));
    }

    @Test
    void not_paid_delivery_is_not_dispatched() {
        saga.prepared(new SagaId("TEST"));
        saga.accepted(new SagaId("TEST"));
        saga.fetched(new SagaId("TEST"));
        //saga.paid(new SagaId("TEST"));

        verifyNoInteractions(dispatchDelivery);
    }

    @Test
    void not_fetched_delivery_is_not_dispatched() {
        saga.prepared(new SagaId("TEST"));
        saga.accepted(new SagaId("TEST"));
        //saga.fetched(new SagaId("TEST"));
        saga.paid(new SagaId("TEST"));

        verifyNoInteractions(dispatchDelivery);
    }

    @Test
    void not_accepted_delivery_is_not_dispatched() {
        saga.prepared(new SagaId("TEST"));
        //saga.accepted(new SagaId("TEST"));
        saga.fetched(new SagaId("TEST"));
        saga.paid(new SagaId("TEST"));

        verifyNoInteractions(dispatchDelivery);
    }

    @Test
    void not_prepared_delivery_is_not_dispatched() {
        //saga.prepared(new SagaId("TEST"));
        saga.accepted(new SagaId("TEST"));
        saga.fetched(new SagaId("TEST"));
        saga.paid(new SagaId("TEST"));

        verifyNoInteractions(dispatchDelivery);
    }
}
