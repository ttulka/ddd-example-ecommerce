package com.ttulka.ecommerce.shipping.dispatching.jdbc;

import com.ttulka.ecommerce.shipping.dispatching.Dispatching;
import com.ttulka.ecommerce.shipping.dispatching.DispatchingSaga;
import com.ttulka.ecommerce.shipping.dispatching.OrderId;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
class DispatchingSagaJdbc implements DispatchingSaga {

    private enum State {
        PREPARED,
        ACCEPTED,
        FETCHED,
        PAID,
        DISPATCHED
    }

    private final @NonNull Dispatching dispatching;
    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public void prepared(OrderId orderId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", orderId.value(), State.PREPARED.name());
        attemptToDispatch(orderId);
    }

    @Override
    public void accepted(OrderId orderId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", orderId.value(), State.ACCEPTED.name());
        attemptToDispatch(orderId);
    }

    @Override
    public void fetched(OrderId orderId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", orderId.value(), State.FETCHED.name());
        attemptToDispatch(orderId);
    }

    @Override
    public void paid(OrderId orderId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", orderId.value(), State.PAID.name());
        attemptToDispatch(orderId);
    }

    private void attemptToDispatch(OrderId orderId) {
        if (isReadyToDispatch(orderId)) {
            dispatch(orderId);
        }
    }

    private boolean isReadyToDispatch(OrderId orderId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(state) FROM dispatching_saga WHERE order_id = ? AND state IN (?, ?, ?, ?)",
                Integer.class, orderId.value(),
                State.PREPARED.name(), State.ACCEPTED.name(), State.FETCHED.name(), State.PAID.name())
               == 4 /* prepared, accepted, fetched, paid */;
    }

    private void dispatch(OrderId orderId) {
        try {
            jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", orderId.value(), State.DISPATCHED.name());

            dispatching.dispatch(orderId);

        } catch (DataIntegrityViolationException e) {
            // this could happen when multiple message come at once (in parallel)
            log.trace("Failed attempt to dispatch: " + orderId, e);
        }
    }
}
