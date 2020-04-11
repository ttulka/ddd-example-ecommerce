package com.ttulka.ecommerce.catalogue.web;

import java.util.List;
import java.util.Map;

import com.ttulka.ecommerce.catalogue.AddCartItem;
import com.ttulka.ecommerce.catalogue.Catalogue;
import com.ttulka.ecommerce.catalogue.ListCartItems;
import com.ttulka.ecommerce.catalogue.RemoveCartItem;
import com.ttulka.ecommerce.catalogue.cart.CartItem;
import com.ttulka.ecommerce.catalogue.cart.Quantity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListCartItems listCartItems;
    @MockBean
    private AddCartItem addCartItem;
    @MockBean
    private RemoveCartItem removeCartItem;
    @MockBean
    private Catalogue catalogue;

    @Test
    void index_shows_the_cart_items() throws Exception {
        when(listCartItems.listCart(any())).thenReturn(List.of(new CartItem("test-1", "Test 1", new Quantity(123))));
        mockMvc.perform(
                get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("items", new Object[]{
                        Map.of("code", "test-1", "title", "Test 1", "quantity", 123)}));
    }

    @Test
    void item_is_added_into_the_cart() throws Exception {
        mockMvc.perform(
                post("/cart")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("productCode", "test-1")
                        .param("quantity", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(addCartItem).intoCart(any(), eq("test-1"), eq(123));
    }

    @Test
    void item_is_removed_from_the_cart() throws Exception {
        mockMvc.perform(
                get("/cart/remove")
                        .param("productCode", "test-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(removeCartItem).fromCart(any(), eq("test-1"));
    }
}
