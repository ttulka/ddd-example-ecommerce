package com.ttulka.ecommerce.catalogue.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ttulka.ecommerce.catalogue.AddCartItem;
import com.ttulka.ecommerce.catalogue.ListCartItems;
import com.ttulka.ecommerce.catalogue.RemoveCartItem;
import com.ttulka.ecommerce.catalogue.cart.Cart;
import com.ttulka.ecommerce.catalogue.cart.CartItem;
import com.ttulka.ecommerce.catalogue.cart.cookies.CartCookies;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Web controller for Cart use-cases.
 */
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
class CartController {

    private final @NonNull ListCartItems listCartItems;
    private final @NonNull AddCartItem addCartItem;
    private final @NonNull RemoveCartItem removeCartItem;

    @GetMapping
    public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        cartIntoModel(new CartCookies(request, response), model);
        return "cart";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String add(@NonNull String productCode, @NonNull Integer quantity,
                      HttpServletRequest request, HttpServletResponse response) {
        addCartItem.intoCart(new CartCookies(request, response), productCode, quantity);

        return "redirect:/cart";
    }

    @GetMapping("/remove")
    public String remove(@NonNull String productCode,
                         HttpServletRequest request, HttpServletResponse response) {
        removeCartItem.fromCart(new CartCookies(request, response), productCode);

        return "redirect:/cart";
    }

    private Map<String, Object> toData(CartItem item) {
        return Map.of("code", item.productCode(),
                      "title", item.title(),
                      "quantity", item.quantity().value());
    }

    private void cartIntoModel(Cart cart, Model model) {
        model.addAttribute("items",
                           listCartItems.listCart(cart).stream()
                                   .map(this::toData)
                                   .toArray());
    }
}
