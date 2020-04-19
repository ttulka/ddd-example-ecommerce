package com.ttulka.ecommerce.shipping.rest;

import java.util.stream.Stream;

import com.ttulka.ecommerce.catalogue.Catalogue;
import com.ttulka.ecommerce.shipping.FindDeliveries;
import com.ttulka.ecommerce.shipping.delivery.Address;
import com.ttulka.ecommerce.shipping.delivery.Delivery;
import com.ttulka.ecommerce.shipping.delivery.DeliveryId;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfo;
import com.ttulka.ecommerce.shipping.delivery.DeliveryInfos;
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
    @MockBean
    private Catalogue catalogue;

    @Test
    void all_deliveries() throws Exception {
        when(findDeliveries.all()).thenReturn(testDeliveryInfos(new DeliveryId("TEST123"), new OrderId("TEST-ORDER1")));

        mockMvc.perform(get("/delivery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("TEST123")))
                .andExpect(jsonPath("$[0].orderId", is("TEST-ORDER1")));
    }

    @Test
    void delivery_by_order() throws Exception {
        when(findDeliveries.byOrderId(eq(new OrderId("TEST-ORDER1")))).thenReturn(
                testDelivery(new DeliveryId("TEST123"), new OrderId("TEST-ORDER1"), "test person", "test place", "test-1", 25));

        mockMvc.perform(get("/delivery/order/TEST-ORDER1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("TEST123")))
                .andExpect(jsonPath("$.address.person", is("test person")))
                .andExpect(jsonPath("$.address.place", is("test place")))
                .andExpect(jsonPath("$.dispatched", is(false)));
    }

    private DeliveryInfos testDeliveryInfos(DeliveryId deliveryId, OrderId orderId) {
        return new DeliveryInfos() {
            @Override
            public Stream<DeliveryInfo> stream() {
                return Stream.of(new DeliveryInfo(deliveryId, orderId));
            }
        };
    }

    private Delivery testDelivery(DeliveryId deliveryId, OrderId orderId,
                                  String person, String place, String productCode, Integer quantity) {
        return new Delivery() {
            @Override
            public DeliveryId id() {
                return deliveryId;
            }

            @Override
            public OrderId orderId() {
                return orderId;
            }

            @Override
            public Address address() {
                return new Address(new Person(person), new Place(place));
            }

            @Override
            public void prepare() {
            }

            @Override
            public void markAsAccepted() {
            }

            @Override
            public void markAsFetched() {
            }

            @Override
            public void markAsPaid() {
            }

            @Override
            public void dispatch() {
            }

            @Override
            public boolean isDispatched() {
                return false;
            }

            @Override
            public boolean isReadyToDispatch() {
                return false;
            }
        };
    }
}
