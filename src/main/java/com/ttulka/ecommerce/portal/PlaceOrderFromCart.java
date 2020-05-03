package com.ttulka.ecommerce.portal;

import java.util.UUID;
import java.util.stream.Collectors;

import com.ttulka.ecommerce.common.primitives.Money;
import com.ttulka.ecommerce.sales.cart.Cart;
import com.ttulka.ecommerce.sales.cart.item.CartItem;
import com.ttulka.ecommerce.sales.order.OrderId;
import com.ttulka.ecommerce.sales.order.PlaceOrder;
import com.ttulka.ecommerce.sales.order.item.OrderItem;
import com.ttulka.ecommerce.sales.order.item.ProductId;

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

    /**
     * Places a new order created from the cart.
     *
     * @param orderId the order ID value
     * @param cart    the cart
     */
    public void placeOrder(@NonNull UUID orderId, @NonNull Cart cart) {
        if (!cart.hasItems()) {
            throw new PlaceOrderFromCart.NoItemsToOrderException();
        }
        // here a command message PlaceOrder could be sent for lower coupling
        placeOrder.place(new OrderId(orderId),
                         cart.items().stream()
                                 .map(this::toOrderItem)
                                 .collect(Collectors.toList()),
                         cart.items().stream()
                                 .map(CartItem::total)
                                 .reduce(Money::add)
                                 .orElse(Money.ZERO));
    }

    private OrderItem toOrderItem(CartItem cartItem) {
        return new OrderItem(new ProductId(cartItem.productId().value()), cartItem.quantity());
    }

    /**
     * NoItemsToOrderException is thrown when there are no items in the cart to be ordered.
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoItemsToOrderException extends RuntimeException {
    }
}
