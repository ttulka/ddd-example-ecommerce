package com.ttulka.ecommerce.warehouse.rest;

import com.ttulka.ecommerce.warehouse.Amount;
import com.ttulka.ecommerce.warehouse.InStock;
import com.ttulka.ecommerce.warehouse.ProductId;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = WarehouseController.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Warehouse warehouse;

    @Test
    void left_in_stock() throws Exception {
        when(warehouse.leftInStock(eq(new ProductId("test-123")))).thenReturn(new InStock(new Amount(456)));

        mockMvc.perform(get("/warehouse/stock/test-123"))
                .andExpect(status().isOk())
                .andExpect(content().string("456"));
    }

    @Test
    void stock_is_listed_for_product_ids() throws Exception {
        when(warehouse.leftInStock(eq(new ProductId("test-1")))).thenReturn(new InStock(new Amount(123)));
        when(warehouse.leftInStock(eq(new ProductId("test-2")))).thenReturn(new InStock(new Amount(456)));

        mockMvc.perform(
                post("/warehouse/stock")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("[\"test-1\", \"test-2\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productId", is("test-1")))
                .andExpect(jsonPath("$[0].inStock", is(123)))
                .andExpect(jsonPath("$[1].productId", is("test-2")))
                .andExpect(jsonPath("$[1].inStock", is(456)));
    }
}
