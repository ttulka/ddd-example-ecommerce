package com.ttulka.ecommerce.billing.payment.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.billing.FindPayments;
import com.ttulka.ecommerce.billing.payment.Payment;
import com.ttulka.ecommerce.billing.payment.PaymentId;
import com.ttulka.ecommerce.billing.payment.Payments;
import com.ttulka.ecommerce.common.events.EventPublisher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@ContextConfiguration(classes = PaymentJdbcConfig.class)
@Sql(statements = "INSERT INTO payments VALUES " +
        "('000', 'REF01', 1.0, 'NEW'), " +
        "('001', 'REF02', 2.0, 'NEW'), " +
        "('002', 'REF03', 3.0, 'NEW')")
class PaymentsTest {

    @Autowired
    private FindPayments findPayments;
    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void payments_are_streamed() {
        Payments payments = findPayments.all();
        List<Payment> list = payments.stream().collect(Collectors.toList());
        assertAll(
                () -> assertThat(list.size()).isEqualTo(3),
                () -> assertThat(list.get(0).id()).isEqualTo(new PaymentId("000")),
                () -> assertThat(list.get(1).id()).isEqualTo(new PaymentId("001")),
                () -> assertThat(list.get(2).id()).isEqualTo(new PaymentId("002"))
        );
    }

    @Test
    void payments_are_limited() {
        Payments payments = findPayments.all()
                .range(2, 1);

        List<Payment> list = payments.stream().collect(Collectors.toList());
        assertAll(
                () -> assertThat(list.size()).isEqualTo(1),
                () -> assertThat(list.get(0).id()).isEqualTo(new PaymentId("002"))
        );
    }

    @Test
    void limited_start_is_greater_than_zero() {
        Payments payments = findPayments.all();
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> payments.range(-1, 1)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> payments.range(1).range(-1, 1))
        );
    }

    @Test
    void limited_limit_is_greater_than_zero() {
        Payments payments = findPayments.all();
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> payments.range(0)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> payments.range(-1)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> payments.range(1).range(0)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> payments.range(1).range(-1))
        );
    }
}
