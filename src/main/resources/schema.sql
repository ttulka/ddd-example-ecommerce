-- ------ CATALOG ------

CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    uri VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    title VARCHAR(20) NOT NULL,
    description VARCHAR(50) NOT NULL DEFAULT(''),
    price DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS products_in_categories (
    product_id VARCHAR(64) NOT NULL,
    category_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (product_id, category_id)
);

-- ------ CART ------

CREATE TABLE IF NOT EXISTS cart_items (
    product_id VARCHAR(64) NOT NULL,
    title VARCHAR(20) NOT NULL,
    price DECIMAL(10,2),
    quantity INT NOT NULL DEFAULT(0),
    cart_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (cart_id, product_id, title, price)
);

-- ------ ORDER ------

CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    total DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS order_items (
    product_id VARCHAR(50) NOT NULL,
    price DECIMAL(10,2),
    quantity INT NOT NULL DEFAULT(0),
    order_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (order_id, product_id, price)
);

-- ------ PAYMENT ------

CREATE TABLE IF NOT EXISTS payments (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    reference_id VARCHAR(64) NOT NULL,
    total DECIMAL(10,2),
    status VARCHAR(50) NOT NULL DEFAULT 'NEW'
);

-- ------ DELIVERY ------

CREATE TABLE IF NOT EXISTS deliveries (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    order_id VARCHAR(64) NOT NULL UNIQUE,
    person VARCHAR(50) NOT NULL,
    place VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS dispatched_deliveries (
    delivery_id VARCHAR(64) NOT NULL PRIMARY KEY
);

-- ------ SHIPPING DISPATCHING SAGA ------

CREATE TABLE IF NOT EXISTS dispatching_saga (
    id VARCHAR(64) NOT NULL,
    state VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, state)
);

-- ------ WAREHOUSE ------

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
