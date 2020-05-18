package com.ttulka.ecommerce.shipping.delivery.rest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;
import com.ttulka.ecommerce.shipping.delivery.PrepareDelivery;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Delivery use-cases.
 */
@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
class DeliveryController {

    private final @NonNull FindDeliveries findDeliveries;
    private final @NonNull PrepareDelivery prepareDelivery;

    @GetMapping
    public List<Map<String, ?>> all() {
        return findDeliveries.all().stream()
                .map(delivery -> Map.of(
                        "id", delivery.id().value(),
                        "orderId", delivery.orderId().value()))
                .collect(Collectors.toList());
    }

    @GetMapping("/order/{orderId}")
    public Map<String, ?> byOrder(@PathVariable @NonNull Object orderId) {
        Delivery delivery = findDeliveries.byOrder(new OrderId(orderId));
        return Map.of(
                "id", delivery.id().value(),
                "address", Map.of(
                        "person", delivery.address().person().value(),
                        "place", delivery.address().place().value()),
                "dispatched", delivery.isDispatched());
    }

    @PostMapping
    public void prepare(@RequestBody @NonNull PrepareRequest request) {
        prepareDelivery.prepare(
                new OrderId(request.orderId),
                new Address(
                        new Person(request.name),
                        new Place(request.address)));
    }

    @Data
    @NoArgsConstructor
    static class PrepareRequest {
        @NonNull String orderId;
        @NonNull String name;
        @NonNull String address;
    }
}
