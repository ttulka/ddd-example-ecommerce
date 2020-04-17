package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfos;
import com.ttulka.ecommerce.shipping.delivery.DeliveryItem;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;
import com.ttulka.ecommerce.shipping.delivery.ProductCode;
import com.ttulka.ecommerce.shipping.delivery.Quantity;

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
                "SELECT id, order_id orderId, person, place, status FROM deliveries " +
                "WHERE order_id = ?", orderId.value())
                .stream().findAny();

        return delivery
                .map(d -> {
                    var items = jdbcTemplate.queryForList(
                            "SELECT product_code productCode, quantity FROM delivery_items " +
                            "WHERE delivery_id = ?", d.get("id"));
                    return toDelivery(d, items);
                })
                .orElseGet(() -> new UnknownDelivery());
    }

    @Override
    public boolean isPrepared(OrderId orderId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(order_id) FROM deliveries " +
                "WHERE order_id = ?", Integer.class, orderId.value()) > 0;
    }

    private Delivery toDelivery(
            Map<String, Object> delivery, List<Map<String, Object>> items) {
        return new DeliveryJdbc(
                new DeliveryId(delivery.get("id")),
                new OrderId(delivery.get("orderId")),
                items.stream()
                        .map(item -> new DeliveryItem(
                                new ProductCode((String) item.get("productCode")),
                                new Quantity((Integer) item.get("quantity"))))
                        .collect(Collectors.toList()),
                new Address(
                        new Person((String) delivery.get("person")),
                        new Place((String) delivery.get("place"))),
                Enum.valueOf(DeliveryJdbc.Status.class, (String) delivery.get("status")),
                jdbcTemplate, eventPublisher);
    }
}
