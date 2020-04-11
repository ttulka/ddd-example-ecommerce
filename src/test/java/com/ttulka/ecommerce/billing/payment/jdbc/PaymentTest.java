package com.ttulka.ecommerce.billing.payment.jdbc;

import com.ttulka.ecommerce.billing.PaymentCollected;
import com.ttulka.ecommerce.billing.payment.Money;
import com.ttulka.ecommerce.billing.payment.Payment;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.common.events.EventPublisher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@JdbcTest
class PaymentTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void payment_values() {
        Payment payment = new PaymentJdbc(
                new ReferenceId(123), new Money(123.5), jdbcTemplate, eventPublisher);
        assertAll(
                () -> assertThat(payment.id()).isNotNull(),
                () -> assertThat(payment.referenceId()).isEqualTo(new ReferenceId(123)),
                () -> assertThat(payment.total()).isEqualTo(new Money(123.5))
        );
    }

    @Test
    void payment_is_requested() {
        Payment payment = new PaymentJdbc(
                new ReferenceId(123), new Money(123.5), jdbcTemplate, eventPublisher);
        payment.request();

        assertThat(payment.isRequested()).isTrue();
        assertThat(payment.isCollected()).isFalse();
    }

    @Test
    void payment_is_collected() {
        Payment payment = new PaymentJdbc(
                new ReferenceId(123), new Money(123.5), jdbcTemplate, eventPublisher);
        payment.request();
        payment.collect();

        assertThat(payment.isRequested()).isTrue();
        assertThat(payment.isCollected()).isTrue();
    }

    @Test
    void collected_payment_raises_an_event() {
        Payment payment = new PaymentJdbc(
                new ReferenceId("TEST123"), new Money(123.5), jdbcTemplate, eventPublisher);
        payment.request();
        payment.collect();

        verify(eventPublisher).raise(argThat(
                event -> {
                    assertThat(event).isInstanceOf(PaymentCollected.class);
                    PaymentCollected paymentCollected = (PaymentCollected) event;
                    assertAll(
                            () -> assertThat(paymentCollected.when).isNotNull(),
                            () -> assertThat(paymentCollected.referenceId).isEqualTo("TEST123")
                    );
                    return true;
                }));
    }

    @Test
    void cannot_request_already_requested_payment() {
        Payment payment = new PaymentJdbc(
                new ReferenceId(123), new Money(123.5), jdbcTemplate, eventPublisher);
        payment.request();

        assertThrows(Payment.PaymentAlreadyRequestedException.class, () -> payment.request());
    }

    @Test
    void cannot_request_already_collected_payment() {
        Payment payment = new PaymentJdbc(
                new ReferenceId(123), new Money(123.5), jdbcTemplate, eventPublisher);
        payment.request();
        payment.collect();

        assertThrows(Payment.PaymentAlreadyRequestedException.class, () -> payment.request());
    }

    @Test
    void cannot_collect_unrequested_payment() {
        Payment payment = new PaymentJdbc(
                new ReferenceId(123), new Money(123.5), jdbcTemplate, eventPublisher);

        assertThrows(Payment.PaymentNotRequestedYetException.class, () -> payment.collect());
    }

    @Test
    void cannot_collect_already_collected_payment() {
        Payment payment = new PaymentJdbc(
                new ReferenceId(123), new Money(123.5), jdbcTemplate, eventPublisher);
        payment.request();
        payment.collect();

        assertThrows(Payment.PaymentAlreadyCollectedException.class, () -> payment.collect());
    }
}
