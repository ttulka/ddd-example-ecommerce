package com.ttulka.ecommerce.shipping.dispatching.jdbc;

import com.ttulka.ecommerce.shipping.delivery.DispatchDelivery;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.dispatching.DispatchingSaga;
import com.ttulka.ecommerce.shipping.dispatching.SagaId;

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

    private final @NonNull DispatchDelivery dispatchDelivery;

    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public void prepared(SagaId sagaId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", sagaId.value(), State.PREPARED.name());
        attemptToDispatch(sagaId);
    }

    @Override
    public void accepted(SagaId sagaId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", sagaId.value(), State.ACCEPTED.name());
        attemptToDispatch(sagaId);
    }

    @Override
    public void fetched(SagaId sagaId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", sagaId.value(), State.FETCHED.name());
        attemptToDispatch(sagaId);
    }

    @Override
    public void paid(SagaId sagaId) {
        jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", sagaId.value(), State.PAID.name());
        attemptToDispatch(sagaId);
    }

    private void attemptToDispatch(SagaId sagaId) {
        if (isReadyToDispatch(sagaId)) {
            dispatch(sagaId);
        }
    }

    private boolean isReadyToDispatch(SagaId sagaId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(state) FROM dispatching_saga WHERE id = ? AND state IN (?, ?, ?, ?)",
                Integer.class, sagaId.value(),
                State.PREPARED.name(), State.ACCEPTED.name(), State.FETCHED.name(), State.PAID.name())
               == 4 /* prepared, accepted, fetched, paid */;
    }

    private void dispatch(SagaId sagaId) {
        try {
            jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", sagaId.value(), State.DISPATCHED.name());

            // here a command message DispatchDelivery could be sent for lower coupling
            // a saga should not query or modify master data, only its private state
            // this is a shortcut where the saga is calling the Delivery service
            // better would be when the saga just sends a message
            dispatchDelivery.byOrder(new OrderId(sagaId.value()));

        } catch (DataIntegrityViolationException e) {
            // this could happen when multiple message come at once (in parallel)
            log.trace("Failed attempt to dispatch: " + sagaId, e);
        }
    }
}
