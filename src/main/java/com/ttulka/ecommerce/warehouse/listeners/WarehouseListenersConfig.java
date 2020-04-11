package com.ttulka.ecommerce.warehouse.listeners;

import com.ttulka.ecommerce.warehouse.FetchGoods;
import com.ttulka.ecommerce.warehouse.RemoveFetchedGoods;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Warehouse event listeners.
 */
@Configuration
class WarehouseListenersConfig {

    @Bean("warehouse-orderPlacedListener") // a custom name to avoid collision
    OrderPlacedListener orderPlacedListener(FetchGoods fetchGoods) {
        return new OrderPlacedListener(fetchGoods);
    }

    @Bean("warehouse-deliveryDispatchedListener") // a custom name to avoid collision
    DeliveryDispatchedListener deliveryDispatchedListener(RemoveFetchedGoods removeFetchedGoods) {
        return new DeliveryDispatchedListener(removeFetchedGoods);
    }
}
