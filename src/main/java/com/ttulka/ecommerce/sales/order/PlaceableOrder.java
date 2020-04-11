package com.ttulka.ecommerce.sales.order;

/**
 * Placeable Order entity.
 */
public interface PlaceableOrder extends Order {

    /**
     * @throws {@link OrderAlreadyPlacedException} when the order has already been placed
     */
    void place();

    /**
     * OrderAlreadyPlacedException is thrown when an already placed Order is placed.
     */
    final class OrderAlreadyPlacedException extends IllegalStateException {
    }
}
