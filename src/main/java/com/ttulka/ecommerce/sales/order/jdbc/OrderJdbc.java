package com.ttulka.ecommerce.sales.order.jdbc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.OrderPlaced;
import com.ttulka.ecommerce.sales.order.PlaceableOrder;
import com.ttulka.ecommerce.sales.order.item.OrderItem;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

/**
 * JDBC implementation for Order entity.
 */
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "items"})
@Slf4j
final class OrderJdbc implements PlaceableOrder {

    private final @NonNull OrderId id;
    private final @NonNull Collection<OrderItem> items;
    private final @NonNull Money total;

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    private boolean placed = false;

    public OrderJdbc(@NonNull OrderId id, @NonNull Collection<OrderItem> items, @NonNull Money total,
                     @NonNull JdbcTemplate jdbcTemplate, @NonNull EventPublisher eventPublisher) {
        if (items.isEmpty()) {
            throw new OrderHasNoItemsException();
        }
        this.id = id;
        this.items = items;
        this.total = total;
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public OrderId id() {
        return id;
    }

    @Override
    public List<OrderItem> items() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    @Override
    public Money total() {
        return total;
    }

    @Override
    public void place() {
        if (placed) {
            throw new OrderAlreadyPlacedException();
        }
        jdbcTemplate.update("INSERT INTO orders VALUES (?, ?)", id.value(), total.value());

        items.forEach(item -> jdbcTemplate.update(
                "INSERT INTO order_items VALUES (?, ?, ?, ?)",
                item.productId().value(), item.unitPrice().value(), item.quantity().value(), id.value()));
        placed = true;

        eventPublisher.raise(toOrderPlaced());
        log.info("Order placed: {}", this);
    }

    private OrderPlaced toOrderPlaced() {
        return new OrderPlaced(
                Instant.now(),
                id.value(),
                items.stream().collect(groupingBy(
                        item -> item.productId().value(),
                        summingInt(item -> item.quantity().value()))),
                total.value());
    }
}
