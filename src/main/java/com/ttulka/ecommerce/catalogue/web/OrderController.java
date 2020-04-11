package com.ttulka.ecommerce.catalogue.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ttulka.ecommerce.catalogue.PlaceOrderFromCart;
import com.ttulka.ecommerce.catalogue.cart.cookies.CartCookies;
import com.ttulka.ecommerce.sales.order.customer.Address;
import com.ttulka.ecommerce.sales.order.customer.Customer;
import com.ttulka.ecommerce.sales.order.customer.Name;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Web controller for Order use-cases.
 */
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
class OrderController {

    private final @NonNull PlaceOrderFromCart placeOrderFromCart;

    @GetMapping
    public String index() {
        return "order";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String place(@NonNull String name, @NonNull String address,
                        HttpServletRequest request, HttpServletResponse response) {
        placeOrderFromCart.placeOrder(
                new CartCookies(request, response),
                new Customer(
                        new Name(name),
                        new Address(address)));

        return "redirect:/order/success";
    }

    @GetMapping("/success")
    public String success(HttpServletRequest request, HttpServletResponse response) {
        new CartCookies(request, response).empty();

        return "order-success";
    }

    @GetMapping("/error")
    public String error(String message, Model model) {
        model.addAttribute("messageCode", message);
        return "order-error";
    }

    @ExceptionHandler({PlaceOrderFromCart.NoItemsToOrderException.class, IllegalArgumentException.class})
    String exception(Exception ex) {
        return "redirect:/order/error?message=" + errorCode(ex);
    }

    private String errorCode(Exception e) {
        if (e instanceof PlaceOrderFromCart.NoItemsToOrderException) {
            return "noitems";
        }
        if (e instanceof IllegalArgumentException) {
            return "requires";
        }
        return "default";
    }
}
