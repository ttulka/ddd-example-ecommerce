package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.Map;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfos;
import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation for Delivery use-cases.
 */
@RequiredArgsConstructor
@Slf4j
class FindDeliveriesJdbc implements FindDeliveries {

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    @Override
    public DeliveryInfos all() {
        return new DeliveryInfosJdbc(
                "SELECT id, order_id orderId FROM deliveries", jdbcTemplate);
    }

    @Transactional
    @Override
    public Delivery byOrderId(OrderId orderId) {
        var delivery = jdbcTemplate.queryForList(
                "SELECT id, order_id orderId, person, place, prepared, accepted, fetched, paid, dispatched FROM deliveries " +
                "WHERE order_id = ?", orderId.value())
                .stream().findAny();

        return delivery
                .map(this::toDelivery)
                .orElseGet(() -> new UnknownDelivery());
    }

    @Override
    public boolean isPrepared(OrderId orderId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(order_id) FROM deliveries " +
                "WHERE order_id = ?", Integer.class, orderId.value()) > 0;
    }

    private Delivery toDelivery(Map<String, Object> delivery) {
        return new DeliveryJdbc(
                new DeliveryId(delivery.get("id")),
                new OrderId(delivery.get("orderId")),
                new Address(
                        new Person((String) delivery.get("person")),
                        new Place((String) delivery.get("place"))),
                (Boolean) delivery.get("prepared"),
                (Boolean) delivery.get("accepted"),
                (Boolean) delivery.get("fetched"),
                (Boolean) delivery.get("paid"),
                (Boolean) delivery.get("dispatched"),
                jdbcTemplate, eventPublisher);
    }
}
