TRUNCATE TABLE products;
TRUNCATE TABLE products_in_stock;
TRUNCATE TABLE deliveries;
TRUNCATE TABLE payments;

INSERT INTO products VALUES
    ('p-1', 'Prod 1', 'Prod 1 Desc', 1.00),
    ('p-2', 'Prod 2', 'Prod 2 Desc', 2.00),
    ('p-3', 'Prod 3', 'Prod 3 Desc', 3.50);

INSERT INTO products_in_stock VALUES
    ('p-1', 1000),
    ('p-2', 1000),
    ('p-3', 1000);