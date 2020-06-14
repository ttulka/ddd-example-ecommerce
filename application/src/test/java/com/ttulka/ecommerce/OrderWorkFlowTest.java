package com.ttulka.ecommerce;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.http.ContentType;
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

    @Test
    void order_is_shipped() throws Exception {
        with() // place order
                .port(port)
                .basePath("/order")
                .contentType(ContentType.JSON)
                .body("{" +
                      "\"orderId\": \"order-1\"," +
                      "\"total\": 123.5," +
                      "\"items\": [{" +
                          "\"productId\": \"p-1\"," +
                          "\"quantity\": 5" +
                      "}]" +
                      "}")
                .post()
                .andReturn();

        with() // prepare delivery
                .port(port)
                .basePath("/delivery")
                .contentType(ContentType.JSON)
                .body("{" +
                      "\"orderId\": \"order-1\"," +
                      "\"name\": \"Test Name\"," +
                      "\"address\": \"Test Address 123\"" +
                      "}")
                .post()
                .andReturn();

        // delivery is dispatched

        Thread.sleep(120);  // wait for all message to come

        Object orderId = with().log().ifValidationFails()
                .port(port)
                .basePath("/delivery")
                .get()
                .andReturn()
                .jsonPath().getMap("[0]").get("orderId");

        assertThat(orderId).isEqualTo("order-1").as("No delivery found for a new order.");

        JsonPath deliveryJson = with().log().ifValidationFails()
                .port(port)
                .basePath("/delivery")
                .get("/order/order-1")
                .andReturn()
                .jsonPath();

        assertAll(
                () -> assertThat(deliveryJson.getBoolean("dispatched")).isTrue().as("Delivery is not dispatched."),
                () -> assertThat(deliveryJson.getMap("address").get("person")).isEqualTo("Test Name"),
                () -> assertThat(deliveryJson.getMap("address").get("place")).isEqualTo("Test Address 123"));
    }

    @Test
    void dispatched_items_are_removed_from_stock() throws Exception {
        with() // place order
                .port(port)
                .basePath("/order")
                .contentType(ContentType.JSON)
                .body("{" +
                      "\"orderId\": \"order-2\"," +
                      "\"total\": 123.5," +
                      "\"items\": [{" +
                          "\"productId\": \"p-2\"," +
                          "\"quantity\": 123" +
                      "}]" +
                      "}")
                .post()
                .andReturn();

        with() // prepare delivery
                .port(port)
                .basePath("/delivery")
                .contentType(ContentType.JSON)
                .body("{" +
                      "\"orderId\": \"order-2\"," +
                      "\"name\": \"Test Name\"," +
                      "\"address\": \"Test Address 123\"" +
                      "}")
                .post()
                .andReturn();

        Thread.sleep(120);  // wait for all message to come

        // (1000-123) left in stock
        String leftInStock = with().log().ifValidationFails()
                .port(port)
                .basePath("/warehouse/stock")
                .get("p-2")
                .andReturn()
                .body().print();

        assertThat(leftInStock).isEqualTo(String.valueOf(1000 - 123)).as("Items are not removed from the stock.");
    }

    @Test
    void payment_for_an_order_is_collected() throws Exception {
        with() // place order
                .port(port)
                .basePath("/order")
                .contentType(ContentType.JSON)
                .body("{" +
                      "\"orderId\": \"order-3\"," +
                      "\"total\": 123.5," +
                      "\"items\": [{" +
                          "\"productId\": \"p-3\"," +
                          "\"quantity\": 5" +
                      "}]" +
                      "}")
                .post()
                .andReturn();

        with() // prepare delivery
                .port(port)
                .basePath("/delivery")
                .contentType(ContentType.JSON)
                .body("{" +
                      "\"orderId\": \"order-3\"," +
                      "\"name\": \"Test Name\"," +
                      "\"address\": \"Test Address 123\"" +
                      "}")
                .post()
                .andReturn();

        Thread.sleep(120);  // wait for all message to come

        // payment is collected

        Map<String, Object> payment = with().log().ifValidationFails()
                .port(port)
                .basePath("/payment")
                .get()
                .andReturn()
                .jsonPath().getMap("[0]");

        assertAll(
                () -> assertThat(payment.get("collected")).isEqualTo(true).as("Payment is not collected."),
                () -> assertThat(payment.get("total")).isEqualTo(123.5f).as("Payment does not match."));
    }
}