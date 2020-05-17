package com.ttulka.ecommerce.sales.order;

import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.order.item.OrderItem;
import com.ttulka.ecommerce.sales.order.item.ProductId;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    void order_item_is_created() {
        OrderItem orderItem = new OrderItem(new ProductId("test-1"), new Quantity(123));
        assertThat(orderItem.quantity()).isEqualTo(new Quantity(123));
    }
}
