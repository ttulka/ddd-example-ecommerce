package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.List;
import java.util.UUID;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.DeliveryDispatched;
import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryItem;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;
import com.ttulka.ecommerce.shipping.delivery.ProductCode;
import com.ttulka.ecommerce.shipping.delivery.Quantity;

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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@JdbcTest
@ContextConfiguration(classes = DeliveryJdbcConfig.class)
@Sql(statements = "INSERT INTO deliveries VALUES " +
                  "('101', '1001', 'Test Person 1', 'Test Place 1', 'NEW'), " +
                  "('102', '1002', 'Test Person 2', 'Test Place 2', 'PREPARED'), " +
                  "('103', '1003', 'Test Person 3', 'Test Place 3', 'FETCHED'), " +
                  "('104', '1004', 'Test Person 4', 'Test Place 4', 'PAID'), " +
                  "('105', '1005', 'Test Person 5', 'Test Place 5', 'READY'), " +
                  "('106', '1006', 'Test Person 6', 'Test Place 6', 'DISPATCHED');")
class DeliveryTest {

    @Autowired
    private FindDeliveries findDeliveries;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void delivery_has_values() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1001));
        assertAll(
                () -> assertThat(delivery.id()).isEqualTo(new DeliveryId(101)),
                () -> assertThat(delivery.orderId()).isEqualTo(new OrderId(1001)),
                () -> assertThat(delivery.address()).isEqualTo(new Address(new Person("Test Person 1"), new Place("Test Place 1"))),
                () -> assertThat(delivery.isReadyToDispatch()).isFalse(),
                () -> assertThat(delivery.isDispatched()).isFalse()
        );
    }

    @Test
    void delivery_is_found_by_order_id() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1001));

        assertThat(delivery.id()).isEqualTo(new DeliveryId(101));
    }

    @Test
    void delivery_is_prepared(@Autowired JdbcTemplate jdbcTemplate) {
        String randId = UUID.randomUUID().toString();
        new DeliveryJdbc(new DeliveryId(randId), new OrderId(randId),
                         List.of(new DeliveryItem(new ProductCode("test"), new Quantity(123))),
                         new Address(new Person("test"), new Place("test")),
                         DeliveryJdbc.Status.NEW,
                         jdbcTemplate, eventPublisher)
                .prepare();

        Delivery delivery = findDeliveries.byOrderId(new OrderId(randId));

        assertThat(delivery.id()).isEqualTo(new DeliveryId(randId));
    }

    @Test
    void delivery_can_be_prepared_only_once() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1006));

        assertThrows(Delivery.DeliveryAlreadyPreparedException.class, () -> delivery.prepare());
    }

    @Test
    void delivery_is_ready() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1005));

        assertThat(delivery.isReadyToDispatch()).isTrue();
    }

    @Test
    void delivery_was_paid() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1003));
        delivery.markAsPaid();

        assertThat(delivery.isReadyToDispatch()).isTrue();
    }

    @Test
    void delivery_was_paid_multiple_times() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1003));
        delivery.markAsPaid();
        delivery.markAsPaid();

        assertThat(delivery.isReadyToDispatch()).isTrue();
    }

    @Test
    void delivery_was_fetched() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1004));
        delivery.markAsFetched();

        assertThat(delivery.isReadyToDispatch()).isTrue();
    }

    @Test
    void delivery_was_fetched_multiple_times() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1004));
        delivery.markAsFetched();
        delivery.markAsFetched();

        assertThat(delivery.isReadyToDispatch()).isTrue();
    }

    @Test
    void delivery_is_dispatched() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1005));
        delivery.dispatch();

        assertThat(delivery.isDispatched()).isTrue();
    }

    @Test
    void delivery_is_not_ready_to_be_dispatched() {
        assertAll(
                () -> assertThrows(Delivery.DeliveryNotReadyToBeDispatchedException.class,
                                   () -> findDeliveries.byOrderId(new OrderId(11)).dispatch()),
                () -> assertThrows(Delivery.DeliveryNotReadyToBeDispatchedException.class,
                                   () -> findDeliveries.byOrderId(new OrderId(12)).dispatch()),
                () -> assertThrows(Delivery.DeliveryNotReadyToBeDispatchedException.class,
                                   () -> findDeliveries.byOrderId(new OrderId(13)).dispatch()),
                () -> assertThrows(Delivery.DeliveryNotReadyToBeDispatchedException.class,
                                   () -> findDeliveries.byOrderId(new OrderId(14)).dispatch()));
    }

    @Test
    void delivery_can_be_dispatched_only_once() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1005));
        delivery.dispatch();

        assertThrows(Delivery.DeliveryAlreadyDispatchedException.class, () -> delivery.dispatch());
    }

    @Test
    void dispatching_a_delivery_raises_an_event() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(1005));
        delivery.dispatch();

        verify(eventPublisher).raise(argThat(
                event -> {
                    assertThat(event).isInstanceOf(DeliveryDispatched.class);
                    DeliveryDispatched deliveryDispatched = (DeliveryDispatched) event;
                    assertAll(
                            () -> assertThat(deliveryDispatched.when).isNotNull(),
                            () -> assertThat(deliveryDispatched.orderId).isNotNull()
                    );
                    return true;
                }));
    }
}
