package com.ttulka.ecommerce.billing;

import com.ttulka.ecommerce.billing.payment.Payments;

/**
 * Find Payments use-case.
 */
public interface FindPayments {

    /**
     * Finds all payments.
     *
     * @return all payments
     */
    Payments all();
}
