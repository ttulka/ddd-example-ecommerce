package com.ttulka.ecommerce.shipping.delivery.jdbc;

import java.util.Collections;
import java.util.List;

import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryItem;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Null object JDBC implementation for Delivery entity.
 */
@RequiredArgsConstructor
@Slf4j
final class UnknownDelivery implements Delivery {

    @Override
    public DeliveryId id() {
        return new DeliveryId(0);
    }

    @Override
    public OrderId orderId() {
        return new OrderId(0);
    }

    @Override
    public List<DeliveryItem> items() {
        return Collections.emptyList();
    }

    @Override
    public Address address() {
        return new Address(
                new Person("unknown"),
                new Place("unknown"));
    }

    @Override
    public void prepare() {
        // do nothing
    }

    @Override
    public void markAsFetched() {
        // do nothing
    }

    @Override
    public void markAsPaid() {
        // do nothing
    }

    @Override
    public void dispatch() {
        throw new DeliveryNotReadyToBeDispatchedException();
    }

    @Override
    public boolean isDispatched() {
        return false;
    }

    @Override
    public boolean isReadyToDispatch() {
        return false;
    }
}
