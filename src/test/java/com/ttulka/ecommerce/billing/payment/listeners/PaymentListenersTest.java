package com.ttulka.ecommerce.billing.payment.listeners;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ttulka.ecommerce.billing.payment.CollectPayment;
import com.ttulka.ecommerce.billing.payment.FindPayments;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.sales.order.OrderPlaced;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class PaymentListenersTest {

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockBean
    private CollectPayment collectPayment;
    @MockBean
    private FindPayments findPayments;

    @Test
    void on_order_placed_collects_a_payment() throws Exception {
        runTx(() -> eventPublisher.raise(
                new OrderPlaced(Instant.now(), "TEST123", Map.of("test", 2), 123.5f)));

        await().atMost(1200, TimeUnit.MILLISECONDS).untilAsserted(
                () -> verify(collectPayment).collect(new ReferenceId("TEST123"), new Money(123.5f)));
    }

    private void runTx(Runnable runnable) {
        new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                runnable.run();
            }
        });
    }
}
