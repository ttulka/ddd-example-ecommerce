package com.ttulka.ecommerce.warehouse.listeners;

import java.time.Instant;
import java.util.List;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.sales.OrderPlaced;
import com.ttulka.ecommerce.shipping.DeliveryDispatched;
import com.ttulka.ecommerce.warehouse.Amount;
import com.ttulka.ecommerce.warehouse.FetchGoods;
import com.ttulka.ecommerce.warehouse.OrderId;
import com.ttulka.ecommerce.warehouse.ProductCode;
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
    void on_order_placed_fetches_goods() {
        runTx(() -> eventPublisher.raise(
                new OrderPlaced(Instant.now(), "TEST123",
                                List.of(new OrderPlaced.OrderItemData("test-1", "Title", 123.5f, 2)),
                                new OrderPlaced.CustomerData("test name", "test address"))));

        verify(fetchGoods).fetchFromOrder(new OrderId("TEST123"), List.of(new ToFetch(new ProductCode("test-1"), new Amount(2))));
    }

    @Test
    void on_delivery_dispatched_removes_fetched_goods() {
        runTx(() -> eventPublisher.raise(new DeliveryDispatched(Instant.now(), "TEST123")));

        verify(removeFetchedGoods).removeForOrder(new OrderId("TEST123"));
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
