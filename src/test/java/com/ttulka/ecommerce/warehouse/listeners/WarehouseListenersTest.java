package com.ttulka.ecommerce.warehouse.listeners;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.order.OrderPlaced;
import com.ttulka.ecommerce.shipping.delivery.DeliveryDispatched;
import com.ttulka.ecommerce.warehouse.Amount;
import com.ttulka.ecommerce.warehouse.FetchGoods;
import com.ttulka.ecommerce.warehouse.OrderId;
import com.ttulka.ecommerce.warehouse.ProductId;
import com.ttulka.ecommerce.warehouse.RemoveFetchedGoods;
import com.ttulka.ecommerce.warehouse.ToFetch;

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
class WarehouseListenersTest {

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockBean
    private FetchGoods fetchGoods;
    @MockBean
    private RemoveFetchedGoods removeFetchedGoods;

    @Test
    void on_order_placed_fetches_goods() throws Exception {
        runTx(() -> eventPublisher.raise(
                new OrderPlaced(Instant.now(), "TEST123", Map.of("test-1", 2), 246.f)));

        await().atMost(1200, TimeUnit.MILLISECONDS).untilAsserted(
                () -> verify(fetchGoods).fetchFromOrder(
                        new OrderId("TEST123"),
                        List.of(new ToFetch(new ProductId("test-1"), new Amount(2)))));
    }

    @Test
    void on_delivery_dispatched_removes_fetched_goods() throws Exception {
        runTx(() -> eventPublisher.raise(new DeliveryDispatched(Instant.now(), "TEST123")));

        await().atMost(1200, TimeUnit.MILLISECONDS).untilAsserted(
                () -> verify(removeFetchedGoods).removeForOrder(new OrderId("TEST123")));
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
