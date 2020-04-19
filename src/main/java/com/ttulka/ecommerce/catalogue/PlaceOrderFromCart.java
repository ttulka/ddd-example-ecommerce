package com.ttulka.ecommerce.catalogue;

import java.util.UUID;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.catalogue.cart.Cart;
import com.ttulka.ecommerce.catalogue.cart.CartItem;
import com.ttulka.ecommerce.sales.FindProducts;
import com.ttulka.ecommerce.sales.PlaceOrder;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.OrderItem;
import com.ttulka.ecommerce.sales.product.Code;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Place Order From Cart use-case.
 */
@RequiredArgsConstructor
public class PlaceOrderFromCart {

    private final @NonNull PlaceOrder placeOrder;
    private final @NonNull FindProducts findProducts;

    /**
     * Places a new order created from the cart.
     *
     * @param cart the cart
     */
    public void placeOrder(@NonNull UUID orderId, @NonNull Cart cart) {
        if (!cart.hasItems()) {
            throw new PlaceOrderFromCart.NoItemsToOrderException();
        }
        placeOrder.place(new OrderId(orderId),
                         cart.items().stream()
                                 .map(this::toOrderItem)
                                 .collect(Collectors.toList()));
    }

    private OrderItem toOrderItem(CartItem cartItem) {
        return new OrderItem(
                cartItem.productCode(),
                cartItem.title(),
                productPrice(cartItem.productCode()),
                cartItem.quantity().value());
    }

    private float productPrice(String productCode) {
        return findProducts.byCode(new Code(productCode))
                .price().value();
    }

    /**
     * NoItemsToOrderException is thrown when there are no items in the cart to be ordered.
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoItemsToOrderException extends RuntimeException {
    }
}
