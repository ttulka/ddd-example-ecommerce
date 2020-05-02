package com.ttulka.ecommerce.shipping.dispatching.jdbc;

import com.ttulka.ecommerce.shipping.delivery.DispatchDelivery;
import com.ttulka.ecommerce.shipping.delivery.OrderId;
import com.ttulka.ecommerce.shipping.dispatching.DispatchingSaga;
import com.ttulka.ecommerce.shipping.dispatching.SagaId;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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
                "SELECT COUNT(state) FROM dispatching_saga WHERE id = ?", Integer.class, sagaId.value())
               == 4 /* prepared, accepted, fetched, paid */;
    }

    private void dispatch(SagaId sagaId) {
        try {
            jdbcTemplate.update("INSERT INTO dispatching_saga VALUES (?, ?)", sagaId.value(), State.DISPATCHED.name());

            // here a command message DispatchDelivery could be sent for lower coupling
            dispatchDelivery.byOrder(new OrderId(sagaId.value()));

        } catch (DataIntegrityViolationException e) {
            // this could happen when multiple message come at once (in parallel)
        }
    }
}
