CREATE TABLE IF NOT EXISTS cart_items (
    product_id VARCHAR(64) NOT NULL,
    title VARCHAR(20) NOT NULL,
    price DECIMAL(10,2),
    quantity INT NOT NULL DEFAULT(0),
    cart_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (cart_id, product_id, title, price)
);
