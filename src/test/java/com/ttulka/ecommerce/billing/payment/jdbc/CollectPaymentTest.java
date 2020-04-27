package com.ttulka.ecommerce.billing.payment.jdbc;

import com.ttulka.ecommerce.billing.payment.CollectPayment;
import com.ttulka.ecommerce.billing.payment.PaymentCollected;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.common.primitives.Money;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@JdbcTest
@ContextConfiguration(classes = PaymentJdbcConfig.class)
class CollectPaymentTest {

    @Autowired
    private CollectPayment collectPayment;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    void payment_confirmation_raises_an_event() {
        collectPayment.collect(new ReferenceId("TEST123"), new Money(123.5f));

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
}
