package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.UUID;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@ContextConfiguration(classes = DeliveryJdbcConfig.class)
@Sql(statements = {
        "INSERT INTO deliveries VALUES " +
            "('101', '1001', 'Test PersonA', 'Test Place 1'), " +
            "('102', '1002', 'Test PersonB', 'Test Place 2');",
        "INSERT INTO dispatched_deliveries VALUES ('102');"})
class DeliveryTest {

    @Autowired
    private FindDeliveries findDeliveries;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void delivery_has_values() {
        Delivery delivery = findDeliveries.byOrder(new OrderId(1001));
        assertAll(
                () -> assertThat(delivery.id()).isEqualTo(new DeliveryId(101)),
                () -> assertThat(delivery.orderId()).isEqualTo(new OrderId(1001)),
                () -> assertThat(delivery.address()).isEqualTo(new Address(new Person("Test PersonA"), new Place("Test Place 1"))),
                () -> assertThat(delivery.isDispatched()).isFalse()
        );
    }

    @Test
    void delivery_is_found_by_order_id() {
        Delivery delivery = findDeliveries.byOrder(new OrderId(1001));

        assertThat(delivery.id()).isEqualTo(new DeliveryId(101));
    }

    @Test
    void delivery_is_prepared(@Autowired JdbcTemplate jdbcTemplate) {
        String randId = UUID.randomUUID().toString();
        new DeliveryJdbc(new DeliveryId(randId), new OrderId(randId),
                         new Address(new Person("Test Test"), new Place("test")),
                         false,
                         jdbcTemplate, eventPublisher)
                .prepare();

        Delivery delivery = findDeliveries.byOrder(new OrderId(randId));

        assertThat(delivery.id()).isEqualTo(new DeliveryId(randId));
    }

    @Test
    void delivery_can_be_prepared_only_once() {
        Delivery delivery = findDeliveries.byOrder(new OrderId(1001));

        assertThrows(Delivery.DeliveryAlreadyPreparedException.class, () -> delivery.prepare());
    }

    @Test
    void delivery_is_dispatched() {
        Delivery delivery = findDeliveries.byOrder(new OrderId(1001));
        delivery.dispatch();

        assertThat(delivery.isDispatched()).isTrue();
    }

    @Test
    void delivery_can_be_dispatched_only_once() {
        Delivery delivery = findDeliveries.byOrder(new OrderId(1001));
        delivery.dispatch();

        assertThrows(Delivery.DeliveryAlreadyDispatchedException.class, () -> delivery.dispatch());
    }

    @Test
    void dispatched_delivery_can_not_be_dispatched() {
        Delivery delivery = findDeliveries.byOrder(new OrderId(1002));

        assertThrows(Delivery.DeliveryAlreadyDispatchedException.class, () -> delivery.dispatch());
    }
}
