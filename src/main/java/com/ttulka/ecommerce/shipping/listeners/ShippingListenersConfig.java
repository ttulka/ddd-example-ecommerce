package com.ttulka.ecommerce.shipping.listeners;

import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.UpdateDelivery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Shipping event listeners.
 */
@Configuration
class ShippingListenersConfig {

    @Bean("shipping-shippingListener") // a custom name to avoid collision
    ShippingListener shippingListener(UpdateDelivery updateDelivery, FindDeliveries findDeliveries) {
        return new ShippingListener(updateDelivery, findDeliveries);
    }
}
