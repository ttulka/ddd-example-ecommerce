package com.ttulka.ecommerce.warehouse.listeners;

import java.util.stream.Collectors;

import com.ttulka.ecommerce.sales.order.OrderPlaced;
import com.ttulka.ecommerce.warehouse.Amount;
import com.ttulka.ecommerce.warehouse.FetchGoods;
import com.ttulka.ecommerce.warehouse.OrderId;
import com.ttulka.ecommerce.warehouse.ProductId;
import com.ttulka.ecommerce.warehouse.ToFetch;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Warehouse listener for OrderPlaced event.
 */
@Component("warehouse-orderPlacedListener") // a custom name to avoid collision
@RequiredArgsConstructor
class OrderPlacedListener {

    private final @NonNull FetchGoods fetchGoods;

    @TransactionalEventListener
    @Async
    public void on(OrderPlaced event) {
        fetchGoods.fetchFromOrder(
                new OrderId(event.orderId),
                event.items.entrySet().stream()
                        .map(item -> new ToFetch(new ProductId(item.getKey()), new Amount(item.getValue())))
                        .collect(Collectors.toList()));
    }
}
