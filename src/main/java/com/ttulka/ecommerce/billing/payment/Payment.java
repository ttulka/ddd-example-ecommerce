package com.ttulka.ecommerce.billing.payment;

/**
 * Payment entity.
 */
public interface Payment {

    PaymentId id();

    ReferenceId referenceId();

    Money total();

    /**
     * @throws {@link PaymentAlreadyRequestedException} when the payment has already been requested
     */
    void request();

    /**
     * @throws {@link PaymentNotRequestedYetException} when the payment has not been requested yet
     * @throws {@link PaymentAlreadyCollectedException} when the payment has already collected
     */
    void collect();

    boolean isRequested();

    boolean isCollected();

    /**
     * PaymentAlreadyRequestedException is thrown when an already requested Payment is requested.
     */
    final class PaymentAlreadyRequestedException extends IllegalStateException {
    }

    /**
     * PaymentNotRequestedYetException is thrown when a Payment is collected but not requested yet.
     */
    final class PaymentNotRequestedYetException extends IllegalStateException {
    }

    /**
     * PaymentAlreadyCollectedException is thrown when an already collected Payment is collected.
     */
    final class PaymentAlreadyCollectedException extends IllegalStateException {
    }
}
