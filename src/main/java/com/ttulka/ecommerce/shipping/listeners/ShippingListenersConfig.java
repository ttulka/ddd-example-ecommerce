package com.ttulka.ecommerce.shipping.listeners;

import com.ttulka.ecommerce.shipping.PrepareDelivery;
import com.ttulka.ecommerce.shipping.UpdateDelivery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Shipping event listeners.
 */
@Configuration
class ShippingListenersConfig {

    @Bean("shipping-orderPlacedListener") // a custom name to avoid collision
    OrderPlacedListener orderPlacedListener(PrepareDelivery prepareDelivery) {
        return new OrderPlacedListener(prepareDelivery);
    }

    @Bean("shipping-dispatchingListener") // a custom name to avoid collision
    DispatchingListener dispatchingListener(UpdateDelivery updateDelivery) {
        return new DispatchingListener(updateDelivery);
    }
}
