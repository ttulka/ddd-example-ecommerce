package com.ttulka.ecommerce.billing.payment.listeners;

import com.ttulka.ecommerce.billing.payment.CollectPayment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Payment event listeners.
 */
@Configuration
class PaymentListenersConfig {

    @Bean("payment-orderPlacedListener") // a custom name to avoid collision
    OrderPlacedListener orderPlacedListener(CollectPayment collectPayment) {
        return new OrderPlacedListener(collectPayment);
    }
}
