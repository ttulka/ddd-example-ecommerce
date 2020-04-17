package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfo;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = DeliveryJdbcConfig.class)
@Sql("/test-data-shipping-find-deliveries.sql")
class FindDeliveriesTest {

    @Autowired
    private FindDeliveries findDeliveries;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void all_deliveries_are_found() {
        List<DeliveryInfo> deliveries = findDeliveries.all().stream().collect(Collectors.toList());
        assertAll(
                () -> assertThat(deliveries).hasSize(2),
                () -> assertThat(deliveries.get(0).id()).isEqualTo(new DeliveryId(301)),
                () -> assertThat(deliveries.get(0).orderId()).isEqualTo(new OrderId(3001)),
                () -> assertThat(deliveries.get(1).id()).isEqualTo(new DeliveryId(302)),
                () -> assertThat(deliveries.get(1).orderId()).isEqualTo(new OrderId(3002))
        );
    }

    @Test
    void delivery_is_found_by_order_id() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(3001));
        assertAll(
                () -> assertThat(delivery.id()).isEqualTo(new DeliveryId(301)),
                () -> assertThat(delivery.orderId()).isEqualTo(new OrderId(3001)),
                () -> assertThat(delivery.address()).isEqualTo(new Address(new Person("Person 1"), new Place("Place 1"))),
                () -> assertThat(delivery.items()).containsExactly(new DeliveryItem(new ProductCode("test-1"), new Quantity(111))),
                () -> assertThat(delivery.isReadyToDispatch()).isTrue(),
                () -> assertThat(delivery.isDispatched()).isFalse()
        );
    }

    @Test
    void delivery_is_not_found_by_order_id() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId("does not exist"));

        assertThat(delivery.id()).isEqualTo(new DeliveryId(0));
    }

    @Test
    void status_is_merged_with_events_ledger() {
        Delivery delivery = findDeliveries.byOrderId(new OrderId(3002));

        assertThat(delivery.isDispatched()).isTrue();
    }

    @Test
    void delivery_for_an_order_is_prepared() {
        boolean isPrepared = findDeliveries.isPrepared(new OrderId(3001));

        assertThat(isPrepared).isTrue();
    }

    @Test
    void delivery_for_an_order_is_not_prepared() {
        boolean isPrepared = findDeliveries.isPrepared(new OrderId("WRONG"));

        assertThat(isPrepared).isFalse();
    }
}
