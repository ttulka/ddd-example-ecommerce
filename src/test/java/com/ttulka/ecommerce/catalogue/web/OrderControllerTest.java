package com.ttulka.ecommerce.catalogue.web;

import com.ttulka.ecommerce.catalogue.Catalogue;
import com.ttulka.ecommerce.catalogue.PlaceOrderFromCart;
import com.ttulka.ecommerce.catalogue.cart.cookies.CartCookies;
import com.ttulka.ecommerce.sales.order.customer.Address;
import com.ttulka.ecommerce.sales.order.customer.Customer;
import com.ttulka.ecommerce.sales.order.customer.Name;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceOrderFromCart placeOrderFromCart;
    @MockBean
    private Catalogue catalogue;

    @Test
    void index_shows_the_order_form_with_name_and_address_input_fields() throws Exception {
        mockMvc.perform(
                get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<form")))
                .andExpect(content().string(containsString("action=\"/order\"")))
                .andExpect(content().string(containsString("method=\"post\"")))
                .andExpect(content().string(containsString("name=\"name\"")))
                .andExpect(content().string(containsString("name=\"address\"")));
    }

    @Test
    void order_is_placed() throws Exception {
        mockMvc.perform(
                post("/order")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("name", "test name")
                        .param("address", "test address"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/success"));

        verify(placeOrderFromCart).placeOrder(
                new CartCookies(new MockHttpServletRequest(), new MockHttpServletResponse()),
                new Customer(
                        new Name("test name"),
                        new Address("test address")));
    }

    @Test
    void order_form_is_not_filled() throws Exception {
        mockMvc.perform(
                post("/order").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/error?message=requires"));
    }

    @Test
    void success_is_shown() throws Exception {
        mockMvc.perform(get("/order/success"))
                .andExpect(status().isOk());
    }

    @Test
    void error_message_is_shown() throws Exception {
        mockMvc.perform(get("/order/error")
                                .param("message", "testmessage"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("messageCode", "testmessage"));
    }

    @Test
    void error_is_shown_for_no_items() throws Exception {
        doThrow(mock(PlaceOrderFromCart.NoItemsToOrderException.class))
                .when(placeOrderFromCart).placeOrder(any(), any());

        mockMvc.perform(
                post("/order")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("name", "test name")
                        .param("address", "test address")
                        .requestAttr("request", new MockHttpServletRequest()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/error?message=noitems"));
    }
}
