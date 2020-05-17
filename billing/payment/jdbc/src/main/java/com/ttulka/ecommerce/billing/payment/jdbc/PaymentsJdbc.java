package com.ttulka.ecommerce.billing.payment.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.ttulka.ecommerce.billing.payment.Payment;
import com.ttulka.ecommerce.billing.payment.PaymentId;
import com.ttulka.ecommerce.billing.payment.Payments;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.common.primitives.Money;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * JDBC implementation of Payments collection.
 */
@RequiredArgsConstructor
final class PaymentsJdbc implements Payments {

    private static final int UNLIMITED = 1000;

    private final @NonNull String query;
    private final @NonNull List<Object> queryParams;

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    private int start = 0;
    private int limit = UNLIMITED;

    public PaymentsJdbc(@NonNull String query,
                        @NonNull JdbcTemplate jdbcTemplate, @NonNull EventPublisher eventPublisher) {
        this(query, List.of(), jdbcTemplate, eventPublisher);
    }

    @Override
    public Payments range(int start, int limit) {
        if (start < 0 || limit <= 0 || limit - start > UNLIMITED) {
            throw new IllegalArgumentException("Start must be greater than zero, " +
                                               "items count must be greater than zero and less or equal than " + UNLIMITED);
        }
        this.start = start;
        this.limit = limit;
        return this;
    }

    @Override
    public Payments range(int limit) {
        return range(0, limit);
    }

    @Override
    public Stream<Payment> stream() {
        var params = new ArrayList<>(queryParams);
        params.add(start);
        params.add(limit);
        return jdbcTemplate.queryForList(query.concat(" ORDER BY 1 LIMIT ?,?"), params.toArray())
                .stream()
                .map(this::toPayment);
    }

    private Payment toPayment(Map<String, Object> entry) {
        return new PaymentJdbc(
                new PaymentId(entry.get("id")),
                new ReferenceId(entry.get("referenceId")),
                new Money(((BigDecimal) entry.get("total")).floatValue()),
                Enum.valueOf(PaymentJdbc.Status.class, (String) entry.get("status")),
                jdbcTemplate, eventPublisher);
    }
}
