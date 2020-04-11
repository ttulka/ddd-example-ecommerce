package com.ttulka.ecommerce.shipping.delivery.jdbc;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.OrderId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@ContextConfiguration(classes = DeliveryJdbcConfig.class)
@Sql(statements = "INSERT INTO deliveries VALUES " +
                  "('201', '2001', 'Test Person 1', 'Test Place 1', 'FETCHED')," +
                  "('202', '2002', 'Test Person 2', 'Test Place 2', 'PAID')")
class UntrackedDeliveryTest {

    @Autowired
    private FindDeliveries findDeliveries;
    @Autowired
    private StatusTracking statusTracking;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void unknown_delivery_has_values() {
        Delivery unknownDelivery = new UntrackedDelivery(new OrderId(123), statusTracking);
        assertAll(
                () -> assertThat(unknownDelivery.id()).isEqualTo(new DeliveryId(0)),
                () -> assertThat(unknownDelivery.orderId()).isEqualTo(new OrderId(123)),
                () -> assertThat(unknownDelivery.items()).hasSize(0),
                () -> assertThat(unknownDelivery.address()).isNotNull(),
                () -> assertThat(unknownDelivery.isReadyToDispatch()).isFalse(),
                () -> assertThat(unknownDelivery.isDispatched()).isFalse()
        );
    }

    @Test
    void prepare_noop() {
        Delivery unknownDelivery = new UntrackedDelivery(new OrderId(123), statusTracking);
        unknownDelivery.prepare();
    }

    @Test
    void marked_as_fetched() {
        Delivery unknownDelivery = new UntrackedDelivery(new OrderId(2002), statusTracking);
        unknownDelivery.markAsFetched();

        Delivery delivery = findDeliveries.byOrderId(new OrderId(2002));

        assertThat(delivery.isReadyToDispatch());
    }

    @Test
    void marked_as_paid() {
        Delivery unknownDelivery = new UntrackedDelivery(new OrderId(2001), statusTracking);
        unknownDelivery.markAsPaid();

        Delivery delivery = findDeliveries.byOrderId(new OrderId(2001));

        assertThat(delivery.isReadyToDispatch());
    }

    @Test
    void dispatch_throws_an_error() {
        Delivery unknownDelivery = new UntrackedDelivery(new OrderId(123), statusTracking);

        assertThrows(Delivery.DeliveryNotReadyToBeDispatchedException.class,
                     () -> unknownDelivery.dispatch());
    }
}
