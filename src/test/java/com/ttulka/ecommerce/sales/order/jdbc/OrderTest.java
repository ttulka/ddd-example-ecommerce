package com.ttulka.ecommerce.sales.order.jdbc;

import java.util.Collections;
import java.util.List;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.OrderPlaced;
import com.ttulka.ecommerce.sales.order.Order;
import com.ttulka.ecommerce.sales.order.OrderItem;
import com.ttulka.ecommerce.sales.order.PlaceableOrder;
import com.ttulka.ecommerce.sales.order.customer.Address;
import com.ttulka.ecommerce.sales.order.customer.Customer;
import com.ttulka.ecommerce.sales.order.customer.Name;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@JdbcTest
class OrderTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void items_are_returned() {
        Order order = new OrderJdbc(
                List.of(new OrderItem("test-1", "Test 1", 1.f, 1), new OrderItem("test-2", "Test 2", 2.f, 2)),
                new Customer(new Name("test"), new Address("test")),
                jdbcTemplate, eventPublisher);
        assertAll(
                () -> assertThat(order.items()).hasSize(2),
                () -> assertThat(order.items().get(0).code()).isEqualTo("test-1"),
                () -> assertThat(order.items().get(0).title()).isEqualTo("Test 1"),
                () -> assertThat(order.items().get(0).price()).isEqualTo(1.f),
                () -> assertThat(order.items().get(0).quantity()).isEqualTo(1),
                () -> assertThat(order.items().get(1).code()).isEqualTo("test-2"),
                () -> assertThat(order.items().get(1).title()).isEqualTo("Test 2"),
                () -> assertThat(order.items().get(1).price()).isEqualTo(2.f),
                () -> assertThat(order.items().get(1).quantity()).isEqualTo(2)
        );
    }

    @Test
    void customer_is_returned() {
        Order order = new OrderJdbc(
                List.of(new OrderItem("test", "Test", 1.f, 1)),
                new Customer(new Name("test name"), new Address("test address")),
                jdbcTemplate, eventPublisher);
        assertAll(
                () -> Assertions.assertThat(order.customer()).isNotNull(),
                () -> Assertions.assertThat(order.customer().name()).isEqualTo(new Name("test name")),
                () -> Assertions.assertThat(order.customer().address()).isEqualTo(new Address("test address"))
        );
    }

    @Test
    void order_contains_at_least_one_item() {
        assertThrows(Order.OrderHasNoItemsException.class,
                     () -> new OrderJdbc(
                             Collections.emptyList(),
                             new Customer(new Name("test"), new Address("test")),
                             jdbcTemplate, eventPublisher));
    }

    @Test
    void placed_order_raises_an_event() {
        PlaceableOrder order = new OrderJdbc(
                List.of(new OrderItem("test", "Test", 123.5f, 456)),
                new Customer(new Name("test name"), new Address("test address")),
                jdbcTemplate, eventPublisher);
        order.place();

        verify(eventPublisher).raise(argThat(
                event -> {
                    assertThat(event).isInstanceOf(OrderPlaced.class);
                    OrderPlaced orderPlaced = (OrderPlaced) event;
                    assertAll(
                            () -> assertThat(orderPlaced.when).isNotNull(),
                            () -> assertThat(orderPlaced.orderId).isNotNull(),
                            () -> assertThat(orderPlaced.orderItems).hasSize(1),
                            () -> assertThat(orderPlaced.orderItems.get(0).code).isEqualTo("test"),
                            () -> assertThat(orderPlaced.orderItems.get(0).title).isEqualTo("Test"),
                            () -> assertThat(orderPlaced.orderItems.get(0).price).isEqualTo(123.5f),
                            () -> assertThat(orderPlaced.orderItems.get(0).quantity).isEqualTo(456),
                            () -> assertThat(orderPlaced.customer.name).isEqualTo("test name"),
                            () -> assertThat(orderPlaced.customer.address).isEqualTo("test address")
                    );
                    return true;
                }));
    }

    @Test
    void order_can_be_placed_only_once() {
        PlaceableOrder order = new OrderJdbc(
                List.of(new OrderItem("test", "Test", 1.f, 1)),
                new Customer(new Name("test"), new Address("test")),
                jdbcTemplate, eventPublisher);
        order.place();

        assertAll(
                () -> assertThrows(PlaceableOrder.OrderAlreadyPlacedException.class, () -> order.place()),
                () -> verify(eventPublisher, only()).raise(any(OrderPlaced.class))    // not two events
        );
    }
}
