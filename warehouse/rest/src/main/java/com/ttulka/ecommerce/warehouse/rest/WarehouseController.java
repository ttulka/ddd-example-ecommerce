package com.ttulka.ecommerce.warehouse.rest;

import java.util.List;
import java.util.Map;

import com.ttulka.ecommerce.warehouse.ProductId;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Warehouse use-cases.
 */
@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
class WarehouseController {

    private final @NonNull Warehouse warehouse;

    @GetMapping("/stock/{productId}")
    public Integer leftInStock(@PathVariable @NonNull String productId) {
        return warehouse.leftInStock(new ProductId(productId)).amount().value();
    }

    @PostMapping("/stock")
    public Object[] stock(@RequestBody @NonNull List<String> productIds) {
        return productIds.stream()
                .map(ProductId::new)
                .map(pId -> Map.of(
                        "productId", pId.value(),
                        "inStock", warehouse.leftInStock(pId).amount().value()))
                .toArray();
    }
}
