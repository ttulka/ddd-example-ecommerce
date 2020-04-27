package com.ttulka.ecommerce.billing.payment;

import com.ttulka.ecommerce.common.primitives.Money;

/**
 * Collect Payment use-case.
 */
public interface CollectPayment {

    /**
     * Collects a payment.
     *
     * @param referenceId the reference ID for the payment
     * @param total       the total amount of money to be collected
     */
    void collect(ReferenceId referenceId, Money total);
}
