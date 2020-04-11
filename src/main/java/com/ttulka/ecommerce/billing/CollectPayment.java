package com.ttulka.ecommerce.billing;

import com.ttulka.ecommerce.billing.payment.Money;
import com.ttulka.ecommerce.billing.payment.ReferenceId;

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
