package com.ttulka.ecommerce.shipping.delivery.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryPrepared;
import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;
import com.ttulka.ecommerce.shipping.delivery.PrepareDelivery;

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
@ContextConfiguration(classes = DeliveryJdbcConfig.class)
class PrepareDeliveryTest {

    @Autowired
    private FindDeliveries findDeliveries;
    @Autowired
    private PrepareDelivery prepareDelivery;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void delivery_for_order_is_prepared() {
        prepareDelivery.prepare(
                new OrderId("TEST123"),
                new Address(new Person("Test Person"), new Place("Test Address 123")));

        Delivery delivery = findDeliveries.byOrder(new OrderId("TEST123"));

        assertAll(
                () -> assertThat(delivery.orderId()).isEqualTo(new OrderId("TEST123")),
                () -> assertThat(delivery.address()).isEqualTo(new Address(new Person("Test Person"), new Place("Test Address 123")))
        );
    }

    @Test
    void prepared_delivery_raises_an_event() {
        prepareDelivery.prepare(
                new OrderId("TEST123"),
                new Address(new Person("Test Person"), new Place("Test Address 123")));

        verify(eventPublisher).raise(argThat(
                event -> {
                    assertThat(event).isInstanceOf(DeliveryPrepared.class);
                    DeliveryPrepared deliveryPrepared = (DeliveryPrepared) event;
                    assertAll(
                            () -> assertThat(deliveryPrepared.when).isNotNull(),
                            () -> assertThat(deliveryPrepared.orderId).isEqualTo("TEST123")
                    );
                    return true;
                }));
    }
}
