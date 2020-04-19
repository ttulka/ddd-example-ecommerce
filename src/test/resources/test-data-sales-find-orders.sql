TRUNCATE TABLE orders;
TRUNCATE TABLE order_items;

INSERT INTO orders VALUES
    ('1'),
    ('2');

INSERT INTO order_items VALUES
    ('001', 'Prod 1', 123.5, 1.00, 1),
    ('002', 'Prod 2', 321.5, 2.00, 1),
    ('002', 'Prod 2', 321.5, 3.00, 2);
