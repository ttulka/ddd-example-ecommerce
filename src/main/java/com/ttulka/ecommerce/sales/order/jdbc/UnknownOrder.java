package com.ttulka.ecommerce.sales.order.jdbc;

import java.util.Collections;
import java.util.List;

import com.ttulka.ecommerce.sales.order.Order;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.OrderItem;

import lombok.ToString;

/**
 * Null object implementation for Order entity.
 */
@ToString
final class UnknownOrder implements Order {

    @Override
    public OrderId id() {
        return new OrderId(0);
    }

    @Override
    public List<OrderItem> items() {
        return Collections.emptyList();
    }
}
