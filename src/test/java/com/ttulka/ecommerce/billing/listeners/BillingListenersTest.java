package com.ttulka.ecommerce.billing.listeners;

import java.time.Instant;
import java.util.List;

import com.ttulka.ecommerce.billing.CollectPayment;
import com.ttulka.ecommerce.billing.FindPayments;
import com.ttulka.ecommerce.billing.payment.Money;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.OrderPlaced;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class BillingListenersTest {

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockBean
    private CollectPayment collectPayment;
    @MockBean
    private FindPayments findPayments;

    @Test
    void on_order_placed_collects_a_payment() {
        runTx(() -> eventPublisher.raise(
                new OrderPlaced(Instant.now(), "TEST123",
                                List.of(new OrderPlaced.OrderItemData("test", "Title", 123.5f, 2)),
                                new OrderPlaced.CustomerData("test name", "test address"))));

        verify(collectPayment).collect(new ReferenceId("TEST123"), new Money(247.));
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
