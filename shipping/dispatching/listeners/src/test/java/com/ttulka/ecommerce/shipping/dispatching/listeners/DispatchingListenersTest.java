package com.ttulka.ecommerce.shipping.dispatching.listeners;

import java.time.Instant;
import java.util.Collections;

import com.ttulka.ecommerce.billing.payment.PaymentCollected;
import com.ttulka.ecommerce.sales.order.OrderPlaced;
import com.ttulka.ecommerce.shipping.delivery.DeliveryPrepared;
import com.ttulka.ecommerce.shipping.dispatching.DispatchingSaga;
import com.ttulka.ecommerce.shipping.dispatching.OrderId;
import com.ttulka.ecommerce.warehouse.GoodsFetched;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DispatchingListenersTest {

    @Test
    void prepared_called() {
        DispatchingSaga saga = mock(DispatchingSaga.class);
        DispatchingListeners listeners = new DispatchingListeners(saga);

        listeners.on(new DeliveryPrepared(Instant.now(), "TEST123"));

        verify(saga).prepared(new OrderId("TEST123"));
    }

    @Test
    void accepted_called() {
        DispatchingSaga saga = mock(DispatchingSaga.class);
        DispatchingListeners listeners = new DispatchingListeners(saga);

        listeners.on(new OrderPlaced(Instant.now(), "TEST123", Collections.emptyMap(), 1.f));

        verify(saga).accepted(new OrderId("TEST123"));
    }

    @Test
    void fetched_called() {
        DispatchingSaga saga = mock(DispatchingSaga.class);
        DispatchingListeners listeners = new DispatchingListeners(saga);

        listeners.on(new GoodsFetched(Instant.now(), "TEST123"));

        verify(saga).fetched(new OrderId("TEST123"));
    }

    @Test
    void paid_called() {
        DispatchingSaga saga = mock(DispatchingSaga.class);
        DispatchingListeners listeners = new DispatchingListeners(saga);

        listeners.on(new PaymentCollected(Instant.now(), "TEST123"));

        verify(saga).paid(new OrderId("TEST123"));
    }
}
