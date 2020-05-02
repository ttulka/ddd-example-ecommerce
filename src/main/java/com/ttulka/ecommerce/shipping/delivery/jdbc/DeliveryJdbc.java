package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.time.Instant;
import java.util.UUID;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryDispatched;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryPrepared;
import com.ttulka.ecommerce.shipping.delivery.OrderId;

import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            jdbcTemplate.update("INSERT INTO deliveries VALUES (?, ?, ?, ?)",
                                id.value(), orderId.value(), address.person().value(), address.place().value());

        } catch (DataIntegrityViolationException e) {
            throw new DeliveryAlreadyPreparedException();
        }

        eventPublisher.raise(new DeliveryPrepared(Instant.now(), orderId.value()));

        log.info("Delivery prepared: {}", this);
    }

    @Override
    public void dispatch() {
        try {
            jdbcTemplate.update("INSERT INTO dispatched_deliveries VALUES (?)", id.value());

        } catch (DataIntegrityViolationException e) {
            throw new DeliveryAlreadyDispatchedException();
        }
        dispatched = true;

        // do the actual dispatching...

        eventPublisher.raise(new DeliveryDispatched(Instant.now(), orderId.value()));

        log.info("Delivery dispatched: {}", this);
    }

    @Override
    public boolean isDispatched() {
        return dispatched;
    }
}
