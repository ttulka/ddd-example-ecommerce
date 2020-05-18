package com.ttulka.ecommerce.portal.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = PortalController.class)
class PortalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index_works() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
