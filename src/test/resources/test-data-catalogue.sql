TRUNCATE TABLE categories;
TRUNCATE TABLE products;
TRUNCATE TABLE products_in_categories;

INSERT INTO categories VALUES
    ('1', 'cat1', 'Cat 1'),
    ('2', 'cat2', 'Cat 2');

INSERT INTO products VALUES
    ('1', '001', 'Prod 1', 'Prod 1 Desc', 1.00),
    ('2', '002', 'Prod 2', 'Prod 2 Desc', 2.00);

INSERT INTO products_in_categories VALUES
    ('1', '1'),
    ('2', '2');
