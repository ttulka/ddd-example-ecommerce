package com.ttulka.ecommerce.sales.cart.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.common.primitives.Quantity;
import com.ttulka.ecommerce.sales.cart.CartId;
import com.ttulka.ecommerce.sales.cart.RetrieveCart;
import com.ttulka.ecommerce.sales.cart.item.CartItem;
import com.ttulka.ecommerce.sales.cart.item.ProductId;
import com.ttulka.ecommerce.sales.cart.item.Title;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Cart use-cases.
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
class CartController {

    private final @NonNull RetrieveCart retrieveCart;

    @GetMapping
    public Object[] list(HttpServletRequest request, HttpServletResponse response) {
        CartId cartId = new CartIdFromCookies(request, response).cartId();
        return retrieveCart.byId(cartId).items().stream()
                .map(item -> Map.of(
                        "productId", item.productId().value(),
                        "title", item.title().value(),
                        "price", item.total().value(),
                        "quantity", item.quantity().value()))
                .toArray();
    }

    @PostMapping
    public void add(@RequestBody @NonNull Map<String, Object> addRequest,
                    HttpServletRequest request, HttpServletResponse response) {
        CartId cartId = new CartIdFromCookies(request, response).cartId();
        retrieveCart.byId(cartId).add(new CartItem(
                new ProductId(addRequest.get("productId")),
                new Title((String)addRequest.get("title")),
                new Money(((Number)addRequest.get("price")).floatValue()),
                new Quantity((Integer)addRequest.get("quantity"))));
    }

    @DeleteMapping
    public void remove(@NonNull String productId,
                       HttpServletRequest request, HttpServletResponse response) {
        CartId cartId = new CartIdFromCookies(request, response).cartId();
        retrieveCart.byId(cartId).remove(new ProductId(productId));
    }

    @PostMapping("/empty")
    public void empty(HttpServletRequest request, HttpServletResponse response) {
        CartId cartId = new CartIdFromCookies(request, response).cartId();
        retrieveCart.byId(cartId).empty();
    }
}
