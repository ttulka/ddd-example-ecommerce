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

import org.springframework.dao.DataAccessException;
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

    private final @NonNull StatusTracking statusTracking;

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
        try {
            Map<String, Object> delivery = jdbcTemplate.queryForMap(
                    "SELECT id, order_id orderId, person, place, status FROM deliveries " +
                    "WHERE order_id = ?", orderId.value());

            List<Map<String, Object>> items = jdbcTemplate.queryForList(
                    "SELECT product_code productCode, quantity FROM delivery_items " +
                    "WHERE delivery_id = ?", delivery.get("id"));

            if (delivery != null && items != null) {
                return toDelivery(delivery, items, statusTracking.isFetched(orderId), statusTracking.isPaid(orderId));
            }
        } catch (DataAccessException ignore) {
            log.debug("Delivery by order ID {} was not found.", orderId);
        }
        return new UntrackedDelivery(orderId, statusTracking);
    }

    private DeliveryJdbc toDelivery(
            Map<String, Object> delivery, List<Map<String, Object>> items,
            boolean fetched, boolean paid) {
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
                mergedStatus(
                        Enum.valueOf(DeliveryJdbc.Status.class, (String) delivery.get("status")),
                        fetched, paid),
                jdbcTemplate, eventPublisher);
    }

    private DeliveryJdbc.Status mergedStatus(DeliveryJdbc.Status status, boolean fetched, boolean paid) {
        if (fetched || paid) {
            switch (status) {
                case NEW:
                case PREPARED:
                    if (fetched) {
                        return DeliveryJdbc.Status.FETCHED;
                    }
                    if (paid) {
                        return DeliveryJdbc.Status.PAID;
                    }
                case FETCHED:
                    if (paid) {
                        return DeliveryJdbc.Status.READY;
                    }
                    break;
                case PAID:
                    if (fetched) {
                        return DeliveryJdbc.Status.READY;
                    }
                    break;
            }
        }
        return status;
    }
}
