TRUNCATE TABLE orders;
TRUNCATE TABLE order_items;

INSERT INTO orders VALUES
    ('1', 1000.00),
    ('2', 2000.00);

INSERT INTO order_items VALUES
    ('001', 123.5, 1.00, 1),
    ('002', 321.5, 2.00, 1),
    ('002', 321.5, 3.00, 2);
