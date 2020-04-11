package com.ttulka.ecommerce.catalogue;

import java.util.List;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.sales.FindCategories;
import com.ttulka.ecommerce.sales.FindProducts;
import com.ttulka.ecommerce.sales.category.Category;
import com.ttulka.ecommerce.sales.category.Uri;
import com.ttulka.ecommerce.sales.product.Product;
import com.ttulka.ecommerce.warehouse.ProductCode;
import com.ttulka.ecommerce.warehouse.Warehouse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Catalogue use-cases.
 */
@RequiredArgsConstructor
public class Catalogue {

    private static final int MAX_RESULTS = 10;

    private final @NonNull FindCategories findCategories;
    private final @NonNull FindProducts findProducts;
    private final @NonNull Warehouse warehouse;

    public List<CatalogueProductData> allProducts() {
        return findProducts.all()
                .range(MAX_RESULTS).stream()
                .map(this::toData)
                .collect(Collectors.toList());
    }

    public List<CatalogueProductData> productsInCategory(@NonNull String categoryUri) {
        return findProducts.fromCategory(new Uri(categoryUri))
                .range(MAX_RESULTS).stream()
                .map(this::toData)
                .collect(Collectors.toList());
    }

    public List<CatalogueCategoryData> categories() {
        return findCategories.all().stream()
                .map(this::toData)
                .collect(Collectors.toList());
    }

    private CatalogueProductData toData(Product product) {
        return new CatalogueProductData(
                product.code().value(),
                product.title().value(),
                product.description().value(),
                product.price().value(),
                warehouse.leftInStock(new ProductCode(product.code().value())).amount());
    }

    private CatalogueCategoryData toData(Category category) {
        return new CatalogueCategoryData(
                category.uri().value(),
                category.title().value());
    }

    @Value
    public static class CatalogueProductData {

        public final String code;
        public final String title;
        public final String description;
        public final float price;
        public final int inStock;
    }

    @Value
    public static class CatalogueCategoryData {

        public final String uri;
        public final String title;
    }
}
