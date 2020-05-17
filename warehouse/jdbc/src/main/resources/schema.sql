CREATE TABLE IF NOT EXISTS products_in_stock (
    product_id VARCHAR(64) NOT NULL PRIMARY KEY,
    amount INT NOT NULL DEFAULT(0)
);

CREATE TABLE IF NOT EXISTS fetched_products (
    product_id VARCHAR(64) NOT NULL,
    amount INT NOT NULL DEFAULT(0),
    order_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (order_id, product_id)
);
