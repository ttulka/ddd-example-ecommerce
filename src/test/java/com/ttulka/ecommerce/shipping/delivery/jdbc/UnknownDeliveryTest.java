package com.ttulka.ecommerce.shipping.delivery.jdbc;

import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UnknownDeliveryTest {

    @Test
    void unknown_delivery_has_values() {
        Delivery unknownDelivery = new UnknownDelivery();
        assertAll(
                () -> assertThat(unknownDelivery.id()).isEqualTo(new DeliveryId(0)),
                () -> assertThat(unknownDelivery.orderId()).isNotNull(),
                () -> assertThat(unknownDelivery.address()).isNotNull()
        );
    }

    @Test
    void prepare_and_markAsAccepted_and_markAsFetched_and_markAsPaid_noop() {
        Delivery unknownDelivery = new UnknownDelivery();
        unknownDelivery.prepare();
        unknownDelivery.markAsAccepted();
        unknownDelivery.markAsFetched();
        unknownDelivery.markAsPaid();

        assertThat(unknownDelivery.isReadyToDispatch()).isFalse();
    }

    @Test
    void dispatch_throws() {
        assertThrows(Delivery.DeliveryNotReadyToBeDispatchedException.class,
                     () -> new UnknownDelivery().dispatch());
    }

    @Test
    void unknown_delivery_is_not_ready_to_dispatch() {
        assertThat(new UnknownDelivery().isReadyToDispatch()).isFalse();
    }

    @Test
    void unknown_delivery_is_not_dispatched() {
        assertThat(new UnknownDelivery().isDispatched()).isFalse();
    }
}
