package com.ttulka.ecommerce.sales.order.rest;

import java.util.List;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.PlaceOrder;
import com.ttulka.ecommerce.sales.order.item.OrderItem;
import com.ttulka.ecommerce.sales.order.item.ProductId;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceOrder placeOrder;

    @Test
    void order_is_placed() throws Exception {
        mockMvc.perform(
                post("/order")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{" +
                                 "\"orderId\": \"test-1\"," +
                                 "\"total\": 123.5," +
                                 "\"items\": [{" +
                                     "\"productId\": \"p1\"," +
                                     "\"quantity\": 5" +
                                 "}]" +
                                 "}"))
                .andExpect(status().isOk());

        verify(placeOrder).place(eq(new OrderId("test-1")),
                                 eq(List.of(
                                         new OrderItem(new ProductId("p1"), new Quantity(5)))),
                                 eq(new Money(123.5f)));
    }
}
