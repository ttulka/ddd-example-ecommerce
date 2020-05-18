package com.ttulka.ecommerce.sales.cart.rest;

import java.util.List;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.cart.Cart;
import com.ttulka.ecommerce.sales.cart.RetrieveCart;
import com.ttulka.ecommerce.sales.cart.item.CartItem;
import com.ttulka.ecommerce.sales.cart.item.ProductId;
import com.ttulka.ecommerce.sales.cart.item.Title;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveCart retrieveCart;

    @Test
    void items_are_listed() throws Exception {
        Cart cart = mock(Cart.class);
        when(cart.items()).thenReturn(List.of(
                new CartItem(new ProductId("test-1"), new Title("Test"), new Money(1.f), new Quantity(123))));
        when(retrieveCart.byId(any())).thenReturn(cart);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("test-1")))
                .andExpect(jsonPath("$[0].title", is("Test")))
                .andExpect(jsonPath("$[0].price", is(123.0))) /* price x quantity */
                .andExpect(jsonPath("$[0].quantity", is(123)));
    }

    @Test
    void item_is_added_into_the_cart() throws Exception {
        Cart cart = mock(Cart.class);
        when(retrieveCart.byId(any())).thenReturn(cart);

        mockMvc.perform(
                post("/cart")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{" +
                                 "\"productId\": \"test-1\"," +
                                 "\"title\": \"Test\"," +
                                 "\"price\": 10.5," +
                                 "\"quantity\": 123" +
                                 "}"))
                .andExpect(status().isOk());
    }

    @Test
    void item_is_removed_from_the_cart() throws Exception {
        Cart cart = mock(Cart.class);
        when(retrieveCart.byId(any())).thenReturn(cart);

        mockMvc.perform(
                delete("/cart")
                        .param("productId", "test-1"))
                .andExpect(status().isOk());
    }

    @Test
    void cart_is_emptied() throws Exception {
        Cart cart = mock(Cart.class);
        when(retrieveCart.byId(any())).thenReturn(cart);

        mockMvc.perform(
                post("/cart/empty"))
                .andExpect(status().isOk());
    }
}
