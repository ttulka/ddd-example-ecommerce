package com.ttulka.ecommerce.billing.listeners;

import com.ttulka.ecommerce.billing.CollectPayment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Billing event listeners.
 */
@Configuration
class BillingListenersConfig {

    @Bean("billing-orderPlacedListener") // a custom name to avoid collision
    OrderPlacedListener orderPlacedListener(CollectPayment collectPayment) {
        return new OrderPlacedListener(collectPayment);
    }
}
