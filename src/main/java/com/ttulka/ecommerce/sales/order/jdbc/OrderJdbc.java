package com.ttulka.ecommerce.sales.order.jdbc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.OrderPlaced;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.OrderItem;
import com.ttulka.ecommerce.sales.order.PlaceableOrder;
import com.ttulka.ecommerce.sales.order.customer.Customer;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation for Order entity.
 */
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "items", "customer"})
@Slf4j
final class OrderJdbc implements PlaceableOrder {

    private final @NonNull OrderId id;
    private final @NonNull List<OrderItem> items;
    private final @NonNull Customer customer;

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    private boolean placed = false;

    public OrderJdbc(@NonNull OrderId id, @NonNull List<OrderItem> items, @NonNull Customer customer,
                     @NonNull JdbcTemplate jdbcTemplate, @NonNull EventPublisher eventPublisher) {
        if (items.isEmpty()) {
            throw new OrderHasNoItemsException();
        }
        this.id = id;
        this.items = items;
        this.customer = customer;
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }

    public OrderJdbc(@NonNull List<OrderItem> items, @NonNull Customer customer,
                     @NonNull JdbcTemplate jdbcTemplate, @NonNull EventPublisher eventPublisher) {
        this(new OrderId(UUID.randomUUID()), items, customer, jdbcTemplate, eventPublisher);
    }

    @Override
    public OrderId id() {
        return id;
    }

    @Override
    public List<OrderItem> items() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public Customer customer() {
        return customer;
    }

    @Override
    public void place() {
        if (placed) {
            throw new OrderAlreadyPlacedException();
        }
        jdbcTemplate.update("INSERT INTO orders VALUES (?, ?, ?)",
                            id.value(), customer.name().value(), customer.address().value());

        items.forEach(item -> jdbcTemplate.update("INSERT INTO order_items VALUES (?, ?, ?, ?, ?)",
                                                  item.code(), item.title(), item.price(), item.quantity(), id.value()));
        placed = true;

        eventPublisher.raise(new OrderPlaced(Instant.now(),
                                             id.value(),
                                             items.stream()
                                                     .map(item -> new OrderPlaced.OrderItemData(
                                                             item.code(), item.title(), item.price(), item.quantity()))
                                                     .collect(Collectors.toList()),
                                             new OrderPlaced.CustomerData(
                                                     customer.name().value(),
                                                     customer.address().value())));
        log.info("Order placed: {}", this);
    }
}
