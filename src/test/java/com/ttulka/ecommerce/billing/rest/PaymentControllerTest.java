package com.ttulka.ecommerce.billing.rest;

import java.util.stream.Stream;

import com.ttulka.ecommerce.billing.FindPayments;
import com.ttulka.ecommerce.billing.payment.Payment;
import com.ttulka.ecommerce.billing.payment.PaymentId;
import com.ttulka.ecommerce.billing.payment.Payments;
import com.ttulka.ecommerce.billing.payment.ReferenceId;
import com.ttulka.ecommerce.catalogue.Catalogue;
import com.ttulka.ecommerce.common.money.Money;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FindPayments findPayments;
    @MockBean
    private Catalogue catalogue;

    @Test
    void all_payments() throws Exception {
        when(findPayments.all()).thenReturn(
                testPayments(new PaymentId("TEST123"), new ReferenceId("TEST-REF1"), new Money(123.5f)));

        mockMvc.perform(get("/payment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("TEST123")))
                .andExpect(jsonPath("$[0].referenceId", is("TEST-REF1")))
                .andExpect(jsonPath("$[0].total", is(123.5)));
    }

    private Payments testPayments(PaymentId id, ReferenceId referenceId, Money total) {
        return new Payments() {
            @Override
            public Payments range(int start, int limit) {
                return this;
            }

            @Override
            public Payments range(int limit) {
                return this;
            }

            @Override
            public Stream<Payment> stream() {
                return Stream.of(new Payment() {
                    @Override
                    public PaymentId id() {
                        return id;
                    }

                    @Override
                    public ReferenceId referenceId() {
                        return referenceId;
                    }

                    @Override
                    public Money total() {
                        return total;
                    }

                    @Override
                    public void request() {

                    }

                    @Override
                    public void collect() {

                    }

                    @Override
                    public boolean isRequested() {
                        return false;
                    }

                    @Override
                    public boolean isCollected() {
                        return false;
                    }
                });
            }
        };
    }
}
