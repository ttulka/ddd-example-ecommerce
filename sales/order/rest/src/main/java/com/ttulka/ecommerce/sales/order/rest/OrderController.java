package com.ttulka.ecommerce.sales.order.rest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.PlaceOrder;
import com.ttulka.ecommerce.sales.order.item.OrderItem;
import com.ttulka.ecommerce.sales.order.item.ProductId;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Order use-cases.
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
class OrderController {

    private final @NonNull PlaceOrder placeOrder;

    @PostMapping
    public void place(@RequestBody @NonNull PlaceRequest request) {
        placeOrder.place(new OrderId(request.orderId),
                         request.items.stream()
                                 .map(this::toOrderItem)
                                 .collect(Collectors.toList()),
                         new Money(request.total));
    }

    private OrderItem toOrderItem(Map<String, Object> item) {
        return new OrderItem(
                new ProductId(item.get("productId")),
                new Quantity((Integer) item.get("quantity")));
    }

    @Data
    @NoArgsConstructor
    static class PlaceRequest {
        @NonNull String orderId;
        @NonNull Float total;
        @NonNull List<Map<String, Object>> items;
    }
}
