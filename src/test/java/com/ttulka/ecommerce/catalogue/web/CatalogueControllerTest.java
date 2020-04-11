package com.ttulka.ecommerce.catalogue.web;

import java.util.List;

import com.ttulka.ecommerce.catalogue.Catalogue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogueController.class)
class CatalogueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Catalogue catalogue;

    @Test
    void index_has_products() throws Exception {
        when(catalogue.allProducts()).thenReturn(testProducts());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", testProducts()));
    }

    @Test
    void category_has_products() throws Exception {
        when(catalogue.productsInCategory("test-category")).thenReturn(testProducts());

        mockMvc.perform(get("/category/test-category"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", testProducts()));
    }

    private List<Catalogue.CatalogueProductData> testProducts() {
        return List.of(new Catalogue.CatalogueProductData("test", "Test", "test desc", 123.5f, 2));
    }
}
