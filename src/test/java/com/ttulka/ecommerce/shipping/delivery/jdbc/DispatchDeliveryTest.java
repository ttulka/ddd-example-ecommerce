package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.UUID;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DispatchDelivery;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = DeliveryJdbcConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class DispatchDeliveryTest {

    @Autowired
    private FindDeliveries findDeliveries;

    @Autowired
    private PrepareDelivery prepareDelivery;

    @Autowired
    private DispatchDelivery dispatchDelivery;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void delivery_is_dispatched() {
        OrderId orderId = prepareOrder();

        dispatchDelivery.byOrder(orderId);

        Delivery delivery = findDeliveries.byOrder(orderId);

        assertThat(delivery.isDispatched()).isTrue();
    }

    private OrderId prepareOrder() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        prepareDelivery.prepare(orderId, new Address(new Person("Test Test"), new Place("Test Test 123")));
        return orderId;
    }
}
