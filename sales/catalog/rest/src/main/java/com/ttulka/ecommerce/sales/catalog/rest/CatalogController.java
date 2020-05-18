package com.ttulka.ecommerce.sales.catalog.rest;

import java.util.Map;

import com.ttulka.ecommerce.sales.catalog.FindCategories;
import com.ttulka.ecommerce.sales.catalog.FindProducts;
import com.ttulka.ecommerce.sales.catalog.FindProductsFromCategory;
import com.ttulka.ecommerce.sales.catalog.category.Uri;
import com.ttulka.ecommerce.sales.catalog.product.Product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Catalog use-cases.
 */
@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
class CatalogController {

    private static final int MAX_RESULTS = 10;

    private final @NonNull FindProducts products;
    private final @NonNull FindProductsFromCategory fromCategory;
    private final @NonNull FindCategories categories;

    @GetMapping("/categories")
    public Object[] categories() {
        return categories.all().stream()
                .map(category -> Map.of(
                        "uri", category.uri().value(),
                        "title", category.title().value()))
                .toArray();
    }

    @GetMapping("/products")
    public Object[] products() {
        return products.all()
                .range(MAX_RESULTS).stream()
                .map(this::toData)
                .toArray();
    }

    @GetMapping("/products/{categoryUri}")
    public Object[] productsFromCategory(@PathVariable @NonNull String categoryUri) {
        return fromCategory.byUri(new Uri(categoryUri))
                .range(MAX_RESULTS).stream()
                .map(this::toData)
                .toArray();
    }

    private Map<String, Object> toData(Product product) {
        return Map.of(
                "id", product.id().value(),
                "title", product.title().value(),
                "description", product.description().value(),
                "price", product.price().value());
    }
}
