package com.ttulka.ecommerce.catalogue.cart.cookies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ttulka.ecommerce.catalogue.cart.Cart;
import com.ttulka.ecommerce.catalogue.cart.CartItem;
import com.ttulka.ecommerce.catalogue.cart.Quantity;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Cookies implementation for Cart entity.
 */
@EqualsAndHashCode(of = "cookie")
public final class CartCookies implements Cart {

    private final @NonNull HttpServletResponse response;

    private String cookie;

    public CartCookies(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        this.response = response;
        Cookie[] cookies = request.getCookies();
        this.cookie = cookies != null
                      ? Arrays.stream(cookies)
                              .filter(c -> "cart".equals(c.getName()))
                              .map(Cookie::getValue)
                              .findAny()
                              .orElse("")
                      : "";
    }

    @Override
    public List<CartItem> items() {
        return Collections.unmodifiableList(parsedItems(cookie));
    }

    @Override
    public boolean hasItems() {
        return cookie != null && !cookie.isBlank();
    }

    @Override
    public void add(@NonNull CartItem toAdd) {
        var currentItems = parsedItems(cookie);

        Quantity alreadyInCart = currentItems.stream()
                .filter(toAdd::equals)
                .map(CartItem::quantity)
                .findAny().orElse(new Quantity(0));

        var items = new ArrayList<>(
                currentItems.stream()
                        .filter(Predicate.not(toAdd::equals))
                        .collect(Collectors.toList()));

        items.add(toAdd.add(alreadyInCart));

        response.addCookie(cartCookie(cookie = items.stream()
                .map(item -> String.format("%s|%s|%d",
                                           item.productCode(),
                                           item.title().replace(" ", "_"),
                                           item.quantity().value()))
                .collect(Collectors.joining("#"))));
    }

    @Override
    public void remove(@NonNull String productCode) {
        response.addCookie(cartCookie(cookie = parsedItems(cookie).stream()
                .filter(item -> !item.productCode().equals(productCode))
                .map(item -> String.format("%s|%s|%d",
                                           item.productCode(),
                                           item.title().replace(" ", "_"),
                                           item.quantity().value()))
                .collect(Collectors.joining("#"))));
    }

    @Override
    public void empty() {
        response.addCookie(cartCookie(cookie = ""));
    }

    private List<CartItem> parsedItems(String cookie) {
        return Arrays.stream(cookie.split("#"))
                .filter(Predicate.not(String::isBlank))
                .map(this::parsedItem)
                .collect(Collectors.toList());
    }

    private CartItem parsedItem(String cookie) {
        String[] item = cookie.split("\\|");
        return new CartItem(item[0], item[1].replace("_", " "), new Quantity(Integer.parseInt(item[2])));
    }

    private Cookie cartCookie(String value) {
        var cookie = new Cookie("cart", value);
        cookie.setPath("/");
        return cookie;
    }
}
