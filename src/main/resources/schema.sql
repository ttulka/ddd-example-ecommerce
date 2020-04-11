-- ------ SALES ------

CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    uri VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(20) NOT NULL,
    description VARCHAR(50) NOT NULL DEFAULT(''),
    price DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS products_in_categories (
    product_id VARCHAR(64) NOT NULL,
    category_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (product_id, category_id)
);

CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    customer VARCHAR(50) NOT NULL,
    address VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    product_code VARCHAR(50) NOT NULL,
    title VARCHAR(20) NOT NULL,
    price DECIMAL(10,2),
    quantity INT NOT NULL DEFAULT(0),
    order_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (order_id, product_code)
);

-- ------ BILLING ------

CREATE TABLE IF NOT EXISTS payments (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    reference_id VARCHAR(64) NOT NULL,
    total DECIMAL(10,2),
    status VARCHAR(50) NOT NULL DEFAULT 'NEW'
);

-- ------ SHIPPING ------

CREATE TABLE IF NOT EXISTS deliveries (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    order_id VARCHAR(64) NOT NULL UNIQUE,
    person VARCHAR(50) NOT NULL,
    place VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'NEW'
);

CREATE TABLE IF NOT EXISTS delivery_items (
    product_code VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT(0),
    delivery_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (delivery_id, product_code)
);

CREATE TABLE IF NOT EXISTS delivery_fetched (
    order_id VARCHAR(64) NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS delivery_paid (
    order_id VARCHAR(64) NOT NULL PRIMARY KEY
);

-- ------ WAREHOUSE ------

CREATE TABLE IF NOT EXISTS products_in_stock (
    product_code VARCHAR(50) NOT NULL PRIMARY KEY,
    amount INT NOT NULL DEFAULT(0)
);

CREATE TABLE IF NOT EXISTS fetched_products (
    product_code VARCHAR(50) NOT NULL,
    amount INT NOT NULL DEFAULT(0),
    order_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (order_id, product_code)
);
