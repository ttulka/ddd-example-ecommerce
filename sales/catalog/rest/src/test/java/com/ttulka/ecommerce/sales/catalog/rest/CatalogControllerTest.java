package com.ttulka.ecommerce.sales.catalog.rest;

import java.util.stream.Stream;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.sales.catalog.FindCategories;
import com.ttulka.ecommerce.sales.catalog.FindProducts;
import com.ttulka.ecommerce.sales.catalog.FindProductsFromCategory;
import com.ttulka.ecommerce.sales.catalog.category.Categories;
import com.ttulka.ecommerce.sales.catalog.category.Category;
import com.ttulka.ecommerce.sales.catalog.category.CategoryId;
import com.ttulka.ecommerce.sales.catalog.category.Uri;
import com.ttulka.ecommerce.sales.catalog.product.Description;
import com.ttulka.ecommerce.sales.catalog.product.Product;
import com.ttulka.ecommerce.sales.catalog.product.ProductId;
import com.ttulka.ecommerce.sales.catalog.product.Products;
import com.ttulka.ecommerce.sales.catalog.product.Title;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = CatalogController.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindCategories findCategories;
    @MockBean
    private FindProducts findProducts;
    @MockBean
    private FindProductsFromCategory findProductsFromCategory;

    @Test
    void categories_are_listed() throws Exception {
        Categories categories = testCategories();
        when(findCategories.all()).thenReturn(categories);

        mockMvc.perform(get("/catalog/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].uri", is("test")))
                .andExpect(jsonPath("$[0].title", is("Test")));
    }

    @Test
    void products_are_listed() throws Exception {
        Products products = testProducts();
        when(findProducts.all()).thenReturn(products);

        mockMvc.perform(get("/catalog/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("TEST")))
                .andExpect(jsonPath("$[0].title", is("Test")))
                .andExpect(jsonPath("$[0].description", is("Test")))
                .andExpect(jsonPath("$[0].price", is(1.0)));
    }

    @Test
    void products_from_category_are_listed() throws Exception {
        Products products = testProducts();
        when(findProductsFromCategory.byUri(new Uri("test-category"))).thenReturn(products);

        mockMvc.perform(get("/catalog/products/test-category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("TEST")))
                .andExpect(jsonPath("$[0].title", is("Test")))
                .andExpect(jsonPath("$[0].description", is("Test")))
                .andExpect(jsonPath("$[0].price", is(1.0)));
    }

    private Categories testCategories() {
        Category category = mock(Category.class);
        when(category.id()).thenReturn(new CategoryId("TEST"));
        when(category.uri()).thenReturn(new Uri("test"));
        when(category.title()).thenReturn(new com.ttulka.ecommerce.sales.catalog.category.Title("Test"));
        Categories categories = mock(Categories.class);
        when(categories.stream()).thenReturn(Stream.of(category, category));
        return categories;
    }

    private Products testProducts() {
        Product product = mock(Product.class);
        when(product.id()).thenReturn(new ProductId("TEST"));
        when(product.title()).thenReturn(new Title("Test"));
        when(product.description()).thenReturn(new Description("Test"));
        when(product.price()).thenReturn(new Money(1.f));
        Products products = mock(Products.class);
        when(products.stream()).thenReturn(Stream.of(product, product));
        when(products.range(anyInt())).thenReturn(products);
        return products;
    }
}
