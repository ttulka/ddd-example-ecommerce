package com.ttulka.ecommerce.warehouse;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Warehouse In Stock entity.
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public final class InStock {

    private final Amount amount;

    public Amount amount() {
        return amount;
    }

    public InStock add(Amount addend) {
        return new InStock(amount.add(addend));
    }

    public InStock remove(Amount addend) {
        return new InStock(amount.subtract(addend));
    }

    public boolean hasEnough(Amount amount) {
        return this.amount.compareTo(amount) != -1;
    }

    public Amount needsYet(Amount amount) {
        return this.amount.compareTo(amount) == 1
               ? Amount.ZERO
               : amount.subtract(this.amount);
    }

    public boolean isSoldOut() {
        return amount.equals(Amount.ZERO);
    }
}
