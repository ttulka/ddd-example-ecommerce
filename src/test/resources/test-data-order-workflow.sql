TRUNCATE TABLE products;
TRUNCATE TABLE products_in_stock;
TRUNCATE TABLE deliveries;
TRUNCATE TABLE payments;

INSERT INTO products VALUES
    ('1', 'OW001', 'Prod 1', 'Prod 1 Desc', 1.00),
    ('2', 'OW002', 'Prod 2', 'Prod 2 Desc', 2.00),
    ('3', 'OW003', 'Prod 3', 'Prod 3 Desc', 3.50);

INSERT INTO products_in_stock VALUES
    ('OW001', 1000),
    ('OW002', 1000),
    ('OW003', 1000);