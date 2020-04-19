package com.ttulka.ecommerce.billing.payment.jdbc;

import java.time.Instant;
import java.util.UUID;

import com.ttulka.ecommerce.billing.PaymentCollected;
import com.ttulka.ecommerce.billing.payment.Money;
import com.ttulka.ecommerce.billing.payment.Payment;
import com.ttulka.ecommerce.billing.payment.PaymentId;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.common.events.EventPublisher;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation of Payment entity.
 */
@EqualsAndHashCode(of = "id")
@ToString(of = {"referenceId", "total"})
@Slf4j
final class PaymentJdbc implements Payment {

    enum Status {
        NEW, REQUESTED, RECEIVED
    }

    private final @NonNull PaymentId id;
    private final @NonNull ReferenceId referenceId;
    private final @NonNull Money total;

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    private @NonNull Status status;

    public PaymentJdbc(@NonNull PaymentId id, @NonNull ReferenceId referenceId, @NonNull Money total, @NonNull Status status,
                       @NonNull JdbcTemplate jdbcTemplate, @NonNull EventPublisher eventPublisher) {
        this.id = id;
        this.referenceId = referenceId;
        this.total = total;
        this.status = status;
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }

    public PaymentJdbc(@NonNull ReferenceId referenceId, @NonNull Money total, @NonNull JdbcTemplate jdbcTemplate, @NonNull EventPublisher eventPublisher) {
        this(new PaymentId(UUID.randomUUID()), referenceId, total, Status.NEW, jdbcTemplate, eventPublisher);
    }

    @Override
    public PaymentId id() {
        return id;
    }

    @Override
    public ReferenceId referenceId() {
        return referenceId;
    }

    @Override
    public Money total() {
        return total;
    }

    @Override
    public void request() {
        if (isRequested() || isCollected()) {
            throw new PaymentAlreadyRequestedException();
        }
        status = Status.REQUESTED;

        jdbcTemplate.update(
                "INSERT INTO payments VALUES (?, ?, ?, ?)",
                id.value(), referenceId.value(), total.value(), status.name());

        log.info("Payment requested: {}", this);
    }

    @Override
    public void collect() {
        if (isCollected()) {
            throw new PaymentAlreadyCollectedException();
        }
        if (!isRequested()) {
            throw new PaymentNotRequestedYetException();
        }
        status = Status.RECEIVED;

        jdbcTemplate.update(
                "UPDATE payments SET status = ? WHERE id = ?",
                status.name(), id.value());

        eventPublisher.raise(new PaymentCollected(Instant.now(), referenceId.value()));

        log.info("Payment collected: {}", this);
    }

    @Override
    public boolean isRequested() {
        return Status.REQUESTED == status || Status.RECEIVED == status;
    }

    @Override
    public boolean isCollected() {
        return Status.RECEIVED == status;
    }
}
