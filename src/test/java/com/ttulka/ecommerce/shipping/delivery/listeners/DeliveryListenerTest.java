package com.ttulka.ecommerce.shipping.delivery.listeners;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.ttulka.ecommerce.billing.payment.PaymentCollected;
import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.order.OrderPlaced;
import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.UpdateDelivery;
import com.ttulka.ecommerce.warehouse.GoodsFetched;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class DeliveryListenerTest {

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockBean
    private UpdateDelivery updateDelivery;
    @MockBean
    private FindDeliveries findDeliveries;

    @Test
    void on_order_placed_a_delivery_is_accepted() throws Exception {
        String orderId = UUID.randomUUID().toString();
        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(true);

        runTx(() -> eventPublisher.raise(
                new OrderPlaced(Instant.now(), orderId, Map.of("test-1", 1), 123.5f)));

        Thread.sleep(500);

        verify(updateDelivery).asAccepted(new OrderId(orderId));
    }

    @Test
    void on_order_placed_an_unprepared_is_resent() throws Exception {
        String orderId = UUID.randomUUID().toString();
        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(false);

        runTx(() -> eventPublisher.raise(
                new OrderPlaced(Instant.now(), orderId, Map.of("test-1", 1), 123.5f)));

        verifyNoInteractions(updateDelivery);

        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(true);

        Thread.sleep(120);

        verify(updateDelivery).asAccepted(new OrderId(orderId));
    }

    @Test
    void on_goods_fetched_a_delivery_is_updated() throws Exception {
        String orderId = UUID.randomUUID().toString();
        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(true);

        runTx(() -> eventPublisher.raise(new GoodsFetched(Instant.now(), orderId)));

        Thread.sleep(120);

        verify(updateDelivery).asFetched(new OrderId(orderId));
    }

    @Test
    void on_goods_fetched_an_unprepared_delivery_is_resent() throws Exception {
        String orderId = UUID.randomUUID().toString();
        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(false);

        runTx(() -> eventPublisher.raise(new GoodsFetched(Instant.now(), orderId)));

        verifyNoInteractions(updateDelivery);

        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(true);

        Thread.sleep(120);

        verify(updateDelivery).asFetched(new OrderId(orderId));
    }

    @Test
    void on_payment_received_a_delivery_is_updated() throws Exception {
        String orderId = UUID.randomUUID().toString();
        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(true);

        runTx(() -> eventPublisher.raise(new PaymentCollected(Instant.now(), orderId)));

        Thread.sleep(120);

        verify(updateDelivery).asPaid(new OrderId(orderId));
    }

    @Test
    void on_payment_received_an_unprepared_delivery_is_resent() throws Exception {
        String orderId = UUID.randomUUID().toString();
        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(false);

        runTx(() -> eventPublisher.raise(new PaymentCollected(Instant.now(), orderId)));

        verifyNoInteractions(updateDelivery);

        when(findDeliveries.isPrepared(eq(new OrderId(orderId)))).thenReturn(true);

        Thread.sleep(120);

        verify(updateDelivery).asPaid(new OrderId(orderId));
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
