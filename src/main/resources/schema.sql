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
    id VARCHAR(64) NOT NULL PRIMARY KEY
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
    prepared BOOLEAN NOT NULL DEFAULT FALSE,
    accepted BOOLEAN NOT NULL DEFAULT FALSE,
    fetched BOOLEAN NOT NULL DEFAULT FALSE,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    dispatched BOOLEAN NOT NULL DEFAULT FALSE
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
