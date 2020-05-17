CREATE TABLE IF NOT EXISTS dispatching_saga (
    order_id VARCHAR(64) NOT NULL,
    state VARCHAR(20) NOT NULL,
    PRIMARY KEY (order_id, state)
);
