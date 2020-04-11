package com.ttulka.ecommerce.billing.payment.jdbc;

import com.ttulka.ecommerce.billing.CollectPayment;
import com.ttulka.ecommerce.billing.payment.Money;
import com.ttulka.ecommerce.billing.payment.Payment;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.common.events.EventPublisher;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * JDBC implementation for Collect Payment use-cases.
 */
@RequiredArgsConstructor
class CollectPaymentJdbc implements CollectPayment {

    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void collect(ReferenceId referenceId, Money total) {
        Payment payment = new PaymentJdbc(referenceId, total, jdbcTemplate, eventPublisher);
        payment.request();
        payment.collect();
    }
}
