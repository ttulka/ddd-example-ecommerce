package com.ttulka.ecommerce.shipping.delivery.jdbc;

import com.ttulka.ecommerce.shipping.delivery.OrderId;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Tracking of the Order status for Delivery.
 * <p>
 * This is not part of the domain as it is used only to solve asynchronous nature of status changes
 * (an Order could be paid before the Delivery was even prepared etc.).
 */
@RequiredArgsConstructor
@Slf4j
final class StatusTracking {

    private final @NonNull JdbcTemplate jdbcTemplate;

    public boolean isFetched(OrderId orderId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(order_id) FROM delivery_fetched " +
                "WHERE order_id = ?", Integer.class, orderId.value()) > 0;
    }

    public boolean isPaid(OrderId orderId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(order_id) FROM delivery_paid " +
                "WHERE order_id = ?", Integer.class, orderId.value()) > 0;
    }

    public void markAsFetched(OrderId orderId) {
        try {
            jdbcTemplate.update("INSERT INTO delivery_fetched VALUES (?)", orderId.value());
        } catch (Exception ignore) {
            log.warn("Cannot mark a delivery as fetched for the order " + orderId, ignore);
        }
    }

    public void markAsPaid(OrderId orderId) {
        try {
            jdbcTemplate.update("INSERT INTO delivery_paid VALUES (?)", orderId.value());
        } catch (Exception ignore) {
            log.warn("Cannot mark a delivery as paid for the order " + orderId, ignore);
        }
    }
}
