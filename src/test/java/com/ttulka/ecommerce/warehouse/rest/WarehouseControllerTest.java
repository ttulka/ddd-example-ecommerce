package com.ttulka.ecommerce.warehouse.rest;

import com.ttulka.ecommerce.warehouse.InStock;
import com.ttulka.ecommerce.warehouse.ProductId;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Warehouse warehouse;

    @Test
    void left_in_stock() throws Exception {
        when(warehouse.leftInStock(eq(new ProductId("test-123")))).thenReturn(new InStock(456));

        mockMvc.perform(get("/warehouse/stock/test-123"))
                .andExpect(status().isOk())
                .andExpect(content().string("456"));
    }
}
