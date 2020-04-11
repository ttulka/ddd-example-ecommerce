package com.ttulka.ecommerce.warehouse.jdbc;

import java.time.Instant;
import java.util.Collection;

import com.ttulka.ecommerce.common.events.EventPublisher;
import com.ttulka.ecommerce.warehouse.FetchGoods;
import com.ttulka.ecommerce.warehouse.GoodsFetched;
import com.ttulka.ecommerce.warehouse.GoodsMissed;
import com.ttulka.ecommerce.warehouse.InStock;
import com.ttulka.ecommerce.warehouse.OrderId;
import com.ttulka.ecommerce.warehouse.RemoveFetchedGoods;
import com.ttulka.ecommerce.warehouse.ToFetch;
import com.ttulka.ecommerce.warehouse.Warehouse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC implementation for Fetching Goods use-cases.
 */
@RequiredArgsConstructor
@Slf4j
class GoodsFetchingJdbc implements FetchGoods, RemoveFetchedGoods {

    private final @NonNull Warehouse warehouse;
    private final @NonNull JdbcTemplate jdbcTemplate;
    private final @NonNull EventPublisher eventPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void fetchFromOrder(OrderId orderId, Collection<ToFetch> toFetch) {
        toFetch.forEach(item -> fetch(item, orderId));

        eventPublisher.raise(new GoodsFetched(Instant.now(), orderId.value()));

        log.info("Goods fetched: {}", toFetch);
    }

    private void fetch(ToFetch item, OrderId orderId) {
        InStock inStock = warehouse.leftInStock(item.productCode());
        if (!inStock.hasEnough(item.amount())) {
            eventPublisher.raise(new GoodsMissed(
                    Instant.now(), item.productCode().value(), inStock.needsYet(item.amount()).value()));
        }
        if (!inStock.isSoldOut()) {
            int amountToFetch = Math.min(item.amount().value(), inStock.amount());
            jdbcTemplate.update(
                    "INSERT INTO fetched_products VALUES (?, ?, ?)",
                    item.productCode().value(), amountToFetch, orderId.value());

            jdbcTemplate.update(
                    "UPDATE products_in_stock SET amount = amount - ? WHERE product_code = ?",
                    amountToFetch, item.productCode().value());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void removeForOrder(OrderId orderId) {
        jdbcTemplate.update(
                "DELETE FROM fetched_products WHERE order_id = ?",
                orderId.value());

        log.debug("Fetched goods removed: {}", orderId);
    }
}
