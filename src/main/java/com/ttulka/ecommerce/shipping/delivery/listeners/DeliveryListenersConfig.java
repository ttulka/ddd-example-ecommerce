package com.ttulka.ecommerce.shipping.delivery.listeners;

import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.UpdateDelivery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Delivery event listeners.
 */
@Configuration
class DeliveryListenersConfig {

    @Bean("delivery-deliveryListener") // a custom name to avoid collision
    DeliveryListener deliveryListener(UpdateDelivery updateDelivery, FindDeliveries findDeliveries) {
        return new DeliveryListener(updateDelivery, findDeliveries);
    }
}
