package com.ttulka.ecommerce.catalogue;

import java.util.List;

import com.ttulka.ecommerce.catalogue.cart.Cart;
import com.ttulka.ecommerce.catalogue.cart.CartItem;
import com.ttulka.ecommerce.catalogue.cart.Quantity;
import com.ttulka.ecommerce.sales.FindProducts;
import com.ttulka.ecommerce.sales.product.Code;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Default implementation for Cart use-cases.
 */
@RequiredArgsConstructor
final class CartItemActions implements ListCartItems, AddCartItem, RemoveCartItem {

    private final @NonNull FindProducts findProducts;

    @Override
    public List<CartItem> listCart(@NonNull Cart cart) {
        return cart.items();
    }

    @Override
    public void intoCart(@NonNull Cart cart, @NonNull String productCode, int quantity) {
        var product = findProducts.byCode(new Code(productCode));
        cart.add(new CartItem(product.code().value(), product.title().value(), new Quantity(quantity)));
    }

    @Override
    public void fromCart(Cart cart, String productCode) {
        cart.remove(productCode);
    }
}
