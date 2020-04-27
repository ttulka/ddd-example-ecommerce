package com.ttulka.ecommerce.shipping.delivery.rest;

import java.util.stream.Stream;

import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfo;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfos;
import com.ttulka.ecommerce.shipping.delivery.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.delivery.Person;
import com.ttulka.ecommerce.shipping.delivery.Place;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindDeliveries findDeliveries;

    @Test
    void all_deliveries() throws Exception {
        DeliveryInfos deliveryInfos = testDeliveryInfos(new DeliveryId("TEST123"), new OrderId("TEST-ORDER1"));
        when(findDeliveries.all()).thenReturn(deliveryInfos);

        mockMvc.perform(get("/delivery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("TEST123")))
                .andExpect(jsonPath("$[0].orderId", is("TEST-ORDER1")));
    }

    @Test
    void delivery_by_order() throws Exception {
        Delivery delivery = testDelivery(new DeliveryId("TEST123"), new OrderId("TEST-ORDER1"), "Test Person", "Test Place 123", "test-1", 25);
        when(findDeliveries.byOrderId(eq(new OrderId("TEST-ORDER1")))).thenReturn(delivery);

        mockMvc.perform(get("/delivery/order/TEST-ORDER1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("TEST123")))
                .andExpect(jsonPath("$.address.person", is("Test Person")))
                .andExpect(jsonPath("$.address.place", is("Test Place 123")))
                .andExpect(jsonPath("$.dispatched", is(false)));
    }

    private DeliveryInfos testDeliveryInfos(DeliveryId deliveryId, OrderId orderId) {
        DeliveryInfos deliveryInfos = mock(DeliveryInfos.class);
        when(deliveryInfos.stream()).thenReturn(Stream.of(new DeliveryInfo(deliveryId, orderId)));
        return deliveryInfos;
    }

    private Delivery testDelivery(DeliveryId deliveryId, OrderId orderId,
                                  String person, String place, String productCode, Integer quantity) {
        Delivery delivery = mock(Delivery.class);
        when(delivery.id()).thenReturn(deliveryId);
        when(delivery.orderId()).thenReturn(orderId);
        when(delivery.address()).thenReturn(new Address(new Person(person), new Place(place)));
        return delivery;
    }
}
