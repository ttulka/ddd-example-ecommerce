TRUNCATE TABLE orders;
TRUNCATE TABLE order_items;

INSERT INTO orders VALUES
    ('1', 1000.00),
    ('2', 2000.00);

INSERT INTO order_items VALUES
    ('001', 1.00, 1),
    ('002', 2.00, 1),
    ('002', 3.00, 2);
