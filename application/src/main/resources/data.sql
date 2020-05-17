-- ------ SALES ------

TRUNCATE TABLE categories;
TRUNCATE TABLE products;
TRUNCATE TABLE products_in_categories;

INSERT INTO categories VALUES
    ('1', 'books', 'books'),
    ('2', 'games-toys', 'games and toys'),
    ('3', 'others', 'others');

INSERT INTO products VALUES
    ('1', 'Domain-Driven Design', 'by Eric Evans', 45.00),
    ('2', 'Object Thinking', 'by David West', 35.00),
    ('3', 'Release It!', 'by Michael Nygard', 32.50),
    ('4', 'Chess', 'Deluxe edition of the classic game.', 3.20),
    ('5', 'Domino', 'In black or white.', 1.50),
    ('6', 'Klein bottle', 'Two-dimensional manifold made from glass.', 25.00);

INSERT INTO products_in_categories VALUES
    ('1', '1'),
    ('2', '1'),
    ('3', '1'),
    ('4', '2'),
    ('5', '2'),
    ('6', '3');

-- ------ WAREHOUSE ------

TRUNCATE TABLE products_in_stock;

INSERT INTO products_in_stock VALUES
    ('1', 5),
    ('2', 0),
    ('3', 13),
    ('4', 55),
    ('5', 102),
    ('6', 1);