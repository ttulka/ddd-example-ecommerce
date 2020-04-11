package com.ttulka.ecommerce.sales.order.jdbc;

import java.util.List;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.PlaceOrder;
import com.ttulka.ecommerce.sales.order.OrderItem;
import com.ttulka.ecommerce.sales.order.customer.Customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation for Place Order use-cases.
 */
@RequiredArgsConstructor
@Slf4j
class PlaceOrderJdbc implements PlaceOrder {

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void place(@NonNull List<OrderItem> items, @NonNull Customer customer) {
        new OrderJdbc(items, customer, jdbcTemplate, eventPublisher)
                .place();
    }
}
