package com.ttulka.ecommerce.shipping.delivery;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Delivery Place domain primitive.
 */
@EqualsAndHashCode
@ToString
public final class Place {

    private final @NonNull String place;

    public Place(@NonNull String place) {
        var placeVal = place.strip();
        if (placeVal.isBlank()) {
            throw new IllegalArgumentException("Place cannot be empty!");
        }
        if (placeVal.length() > 100) {
            throw new IllegalArgumentException("Place cannot be longer than 100 characters!");
        }
        this.place = placeVal;
    }

    public String value() {
        return place;
    }
}
