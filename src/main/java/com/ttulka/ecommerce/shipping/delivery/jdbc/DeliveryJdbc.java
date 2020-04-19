package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.time.Instant;
import java.util.UUID;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.DeliveryDispatched;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.OrderId;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation for Delivery entity.
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "orderId", "address"})
@Slf4j
final class DeliveryJdbc implements Delivery {

    private final @NonNull DeliveryId id;
    private final @NonNull OrderId orderId;
    private final @NonNull Address address;

    private boolean prepared;
    private boolean accepted;
    private boolean fetched;
    private boolean paid;
    private boolean dispatched;

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    public DeliveryJdbc(@NonNull OrderId orderId, @NonNull Address address,
                        @NonNull JdbcTemplate jdbcTemplate, @NonNull EventPublisher eventPublisher) {
        this.id = new DeliveryId(UUID.randomUUID());
        this.orderId = orderId;
        this.address = address;
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public DeliveryId id() {
        return id;
    }

    @Override
    public OrderId orderId() {
        return orderId;
    }

    @Override
    public Address address() {
        return address;
    }

    @Override
    public void prepare() {
        if (prepared) {
            throw new DeliveryAlreadyPreparedException();
        }
        prepared = true;

        jdbcTemplate.update("INSERT INTO deliveries VALUES (?, ?, ?, ?, TRUE, FALSE, FALSE, FALSE, FALSE)",
                            id.value(), orderId.value(), address.person().value(), address.place().value());

        log.debug("Delivery prepared: {}", this);
    }

    @Override
    public void markAsAccepted() {
        accepted = true;
        jdbcTemplate.update("UPDATE deliveries SET accepted = TRUE WHERE id = ?", id.value());

        log.debug("Delivery marked as accepted: {}", this);
    }

    @Override
    public void markAsFetched() {
        fetched = true;
        jdbcTemplate.update("UPDATE deliveries SET fetched = TRUE WHERE id = ?", id.value());

        log.debug("Delivery marked as fetched: {}", this);
    }

    @Override
    public void markAsPaid() {
        paid = true;
        jdbcTemplate.update("UPDATE deliveries SET paid = TRUE WHERE id = ?", id.value());

        log.debug("Delivery marked as paid: {}", this);
    }

    @Override
    public void dispatch() {
        if (dispatched) {
            throw new DeliveryAlreadyDispatchedException();
        }
        if (!isReadyToDispatch()) {
            throw new DeliveryNotReadyToBeDispatchedException();
        }
        dispatched = true;
        jdbcTemplate.update("UPDATE deliveries SET dispatched = TRUE WHERE id = ?", id.value());

        eventPublisher.raise(new DeliveryDispatched(Instant.now(), orderId.value()));

        log.info("Delivery dispatched: {}", this);
    }

    @Override
    public boolean isDispatched() {
        return dispatched;
    }

    @Override
    public boolean isReadyToDispatch() {
        return accepted && fetched && paid && !dispatched;
    }
}
