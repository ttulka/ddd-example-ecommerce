CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    total DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS order_items (
    product_id VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT(0),
    order_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (order_id, product_id)
);
