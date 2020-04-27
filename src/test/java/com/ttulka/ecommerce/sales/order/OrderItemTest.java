package com.ttulka.ecommerce.sales.order;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.order.item.OrderItem;
import com.ttulka.ecommerce.sales.order.item.ProductId;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderItemTest {

    @Test
    void order_item_is_created() {
        OrderItem orderItem = new OrderItem(new ProductId("test-1"), new Money(12.34f), new Quantity(123));
        assertAll(
                () -> assertThat(orderItem.unitPrice()).isEqualTo(new Money(12.34f)),
                () -> assertThat(orderItem.quantity()).isEqualTo(new Quantity(123)),
                () -> assertThat(orderItem.total()).isEqualTo(new Money(12.34f * 123))
        );
    }
}
