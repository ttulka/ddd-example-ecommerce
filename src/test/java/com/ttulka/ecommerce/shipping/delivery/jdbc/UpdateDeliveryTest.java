package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.UUID;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;
import com.ttulka.ecommerce.shipping.delivery.PrepareDelivery;
import com.ttulka.ecommerce.shipping.delivery.UpdateDelivery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = DeliveryJdbcConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class UpdateDeliveryTest {

    @Autowired
    private FindDeliveries findDeliveries;

    @Autowired
    private PrepareDelivery prepareDelivery;

    @Autowired
    private UpdateDelivery updateDelivery;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void accepted_delivery_is_not_ready_yet() {
        OrderId orderId = prepareOrder();

        updateDelivery.asAccepted(orderId);

        Delivery delivery = findDeliveries.byOrderId(orderId);
        assertAll(
                () -> assertThat(delivery.isReadyToDispatch()).isFalse(),
                () -> assertThat(delivery.isDispatched()).isFalse()
        );
    }

    @Test
    void fetched_delivery_is_not_ready_yet() {
        OrderId orderId = prepareOrder();

        updateDelivery.asFetched(orderId);

        Delivery delivery = findDeliveries.byOrderId(orderId);
        assertAll(
                () -> assertThat(delivery.isReadyToDispatch()).isFalse(),
                () -> assertThat(delivery.isDispatched()).isFalse()
        );
    }

    @Test
    void paid_delivery_is_not_ready_yet() {
        OrderId orderId = prepareOrder();

        updateDelivery.asPaid(orderId);

        Delivery delivery = findDeliveries.byOrderId(orderId);
        assertAll(
                () -> assertThat(delivery.isReadyToDispatch()).isFalse(),
                () -> assertThat(delivery.isDispatched()).isFalse()
        );
    }

    @Test
    void accepted_and_fetched_and_paid_delivery_is_dispatched() {
        OrderId orderId = prepareOrder();

        updateDelivery.asAccepted(orderId);
        updateDelivery.asFetched(orderId);
        updateDelivery.asPaid(orderId);

        Delivery delivery = findDeliveries.byOrderId(orderId);
        assertAll(
                () -> assertThat(delivery.isReadyToDispatch()).isFalse(),
                () -> assertThat(delivery.isDispatched()).isTrue()
        );
    }

    private OrderId prepareOrder() {
        OrderId orderId = new OrderId(UUID.randomUUID());
        prepareDelivery.prepare(orderId, new Address(new Person("Test Test"), new Place("Test Test 123")));
        return orderId;
    }
}
