package com.ttulka.ecommerce;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("/test-data-order-workflow.sql")
class OrderWorkFlowTest {

    @LocalServerPort
    private int port;

    @BeforeAll
    static void setupRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void order_is_shipped() throws Exception {
        CookieFilter cookieFilter = new CookieFilter(); // share cookies among requests

        with() // add an cart item
                .filter(cookieFilter)
                .port(port)
                .basePath("/cart")
                .param("productId", "p-1")
                .param("title", "Prod 1")
                .param("price", 1.f)
                .param("quantity", 1)
                .post()
                .andReturn();

        with() // place an order
                .filter(cookieFilter)
                .port(port)
                .basePath("/order")
                .formParam("name", "Test Name")
                .formParam("address", "Test Address 123")
                .post()
                .andReturn();

        // delivery is dispatched

        Thread.sleep(120);  // wait for all message to come

        Object orderId = with()
                .port(port)
                .basePath("/delivery")
                .get()
                .andReturn()
                .jsonPath().getMap("[0]").get("orderId");

        assertThat(orderId).isNotNull().as("No delivery found for a new order.");

        JsonPath deliveryJson = with()
                .port(port)
                .basePath("/delivery")
                .get("/order/" + orderId)
                .andReturn()
                .jsonPath();

        assertAll(
                () -> assertThat(deliveryJson.getBoolean("dispatched")).isTrue().as("Delivery is not dispatched."),
                () -> assertThat(deliveryJson.getMap("address").get("person")).isEqualTo("Test Name"),
                () -> assertThat(deliveryJson.getMap("address").get("place")).isEqualTo("Test Address 123"));
    }

    @Test
    void dispatched_items_are_removed_from_stock() throws Exception {
        CookieFilter cookieFilter = new CookieFilter(); // share cookies among requests

        with() // add an cart item
                .filter(cookieFilter)
                .port(port)
                .basePath("/cart")
                .param("productId", "p-2")
                .param("title", "Prod 2")
                .param("price", 2.f)
                .param("quantity", 123)
                .post()
                .andReturn();

        with() // place an order
                .filter(cookieFilter)
                .port(port)
                .basePath("/order")
                .formParam("name", "Test Name")
                .formParam("address", "Test Address 123")
                .post()
                .andReturn();

        Thread.sleep(120);  // wait for all message to come

        // (1000-123) left in stock
        String leftInStock = with()
                .port(port)
                .basePath("/warehouse/stock")
                .get("p-2")
                .andReturn()
                .body().print();

        assertThat(leftInStock).isEqualTo(String.valueOf(1000 - 123)).as("Items are not removed from the stock.");
    }

    @Test
    void payment_for_an_order_is_collected() throws Exception {
        CookieFilter cookieFilter = new CookieFilter(); // share cookies among requests

        with() // add an cart item
                .filter(cookieFilter)
                .port(port)
                .basePath("/cart")
                .param("productId", "p-3")
                .param("title", "Prod 3")
                .param("price", 3.50f)
                .param("quantity", 3)
                .post()
                .andReturn();

        with() // place an order
                .filter(cookieFilter)
                .port(port)
                .basePath("/order")
                .formParam("name", "Test Name")
                .formParam("address", "Test Address 123")
                .post()
                .andReturn();

        Thread.sleep(120);  // wait for all message to come

        // payment is collected

        Map<String, Object> payment = with()
                .port(port)
                .basePath("/payment")
                .get()
                .andReturn()
                .jsonPath().getMap("[0]");

        assertAll(
                () -> assertThat(payment.get("collected")).isEqualTo(true).as("Payment is not collected."),
                () -> assertThat(payment.get("total")).isEqualTo(10.5f).as("Payment does not match."));
    }
}