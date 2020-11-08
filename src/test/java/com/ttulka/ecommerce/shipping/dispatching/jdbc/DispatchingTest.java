package com.ttulka.ecommerce.shipping.dispatching.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.dispatching.DeliveryDispatched;
import com.ttulka.ecommerce.shipping.dispatching.Dispatching;
import com.ttulka.ecommerce.shipping.dispatching.OrderId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@JdbcTest
@ContextConfiguration(classes = DispatchingJdbcConfig.class)
class DispatchingTest {

    @Autowired
    private Dispatching dispatching;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void dispatching_a_delivery_raises_an_event() {
        dispatching.dispatch(new OrderId("DispatchingTest"));

        verify(eventPublisher).raise(argThat(
                event -> {
                    assertThat(event).isInstanceOf(DeliveryDispatched.class);
                    DeliveryDispatched deliveryDispatched = (DeliveryDispatched) event;
                    assertAll(
                            () -> assertThat(deliveryDispatched.when).isNotNull(),
                            () -> assertThat(deliveryDispatched.orderId).isEqualTo("DispatchingTest")
                    );
                    return true;
                }));
    }
}
