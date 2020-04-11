package com.ttulka.ecommerce.catalogue.web;

import java.util.Collections;
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
class WebLayoutAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Catalogue catalogue;

    @Test
    void categories_are_on_every_page() throws Exception {
        when(catalogue.categories()).thenReturn(testCategories());
        when(catalogue.allProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("categories", testCategories()));
    }

    private List<Catalogue.CatalogueCategoryData> testCategories() {
        return List.of(new Catalogue.CatalogueCategoryData("test-uri", "Test"));
    }
}
