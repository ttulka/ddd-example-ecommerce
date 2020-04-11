package com.ttulka.ecommerce.sales.order.jdbc;

import java.util.List;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.OrderPlaced;
import com.ttulka.ecommerce.sales.PlaceOrder;
import com.ttulka.ecommerce.sales.order.OrderItem;
import com.ttulka.ecommerce.sales.order.customer.Address;
import com.ttulka.ecommerce.sales.order.customer.Customer;
import com.ttulka.ecommerce.sales.order.customer.Name;

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
@ContextConfiguration(classes = OrderJdbcConfig.class)
class PlaceOrderTest {

    @Autowired
    private PlaceOrder placeOrder;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void order_placed_raises_an_event() {
        placeOrder.place(
                List.of(new OrderItem("test", "Test", 123.5f, 123)),
                new Customer(new Name("test name"), new Address("test address")));

        verify(eventPublisher).raise(argThat(
                event -> {
                    assertThat(event).isInstanceOf(OrderPlaced.class);
                    OrderPlaced orderPlaced = (OrderPlaced) event;
                    assertAll(
                            () -> assertThat(orderPlaced.when).isNotNull(),
                            () -> assertThat(orderPlaced.customer.name).isEqualTo("test name"),
                            () -> assertThat(orderPlaced.customer.address).isEqualTo("test address"),
                            () -> assertThat(orderPlaced.orderItems).hasSize(1),
                            () -> assertThat(orderPlaced.orderItems.get(0).code).isEqualTo("test"),
                            () -> assertThat(orderPlaced.orderItems.get(0).title).isEqualTo("Test"),
                            () -> assertThat(orderPlaced.orderItems.get(0).price).isEqualTo(123.5f),
                            () -> assertThat(orderPlaced.orderItems.get(0).quantity).isEqualTo(123)
                    );
                    return true;
                }));
    }
}
