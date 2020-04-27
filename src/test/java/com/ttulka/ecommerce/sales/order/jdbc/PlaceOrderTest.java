package com.ttulka.ecommerce.sales.order.jdbc;

import java.util.List;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.OrderPlaced;
import com.ttulka.ecommerce.sales.order.PlaceOrder;
import com.ttulka.ecommerce.sales.order.item.OrderItem;
import com.ttulka.ecommerce.sales.order.item.ProductId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
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
        placeOrder.place(new OrderId("TEST123"), List.of(
                new OrderItem(new ProductId("PTEST"), new Money(123.5f), new Quantity(123))),
                new Money(123.5f * 123));

        verify(eventPublisher).raise(argThat(
                event -> {
                    assertThat(event).isInstanceOf(OrderPlaced.class);
                    OrderPlaced orderPlaced = (OrderPlaced) event;
                    assertAll(
                            () -> assertThat(orderPlaced.when).isNotNull(),
                            () -> assertThat(orderPlaced.items).hasSize(1),
                            () -> assertThat(orderPlaced.items.get("PTEST")).isEqualTo(123),
                            () -> assertThat(orderPlaced.total).isCloseTo(123.5f * 123, offset(0.01f))
                    );
                    return true;
                }));
    }
}
