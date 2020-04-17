package com.ttulka.ecommerce.shipping.listeners;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.shipping.FindDeliveries;
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
    DispatchingListener dispatchingListener(UpdateDelivery updateDelivery, FindDeliveries findDeliveries, ResendEvent resendEvent) {
        return new DispatchingListener(updateDelivery, findDeliveries, resendEvent);
    }

    @Bean
    ResendEvent resendEvent(EventPublisher eventPublisher) {
        return new ResendEvent(eventPublisher);
    }
}
