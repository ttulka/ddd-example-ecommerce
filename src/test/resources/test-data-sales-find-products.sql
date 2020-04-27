TRUNCATE TABLE categories;
TRUNCATE TABLE products;
TRUNCATE TABLE products_in_categories;

INSERT INTO categories VALUES
    ('c-1', 'cat1', 'Cat 1'),
    ('c-2', 'cat2', 'Cat 2');

INSERT INTO products VALUES
    ('p-1', 'Prod 1', 'Prod 1 Desc', 1.00),
    ('p-2', 'Prod 2', 'Prod 2 Desc', 2.00),
    ('p-3', 'Prod 3', 'Prod 3 Desc', 3.50);

INSERT INTO products_in_categories VALUES
    ('p-1', 'c-1'),
    ('p-2', 'c-1'),
    ('p-3', 'c-2');
