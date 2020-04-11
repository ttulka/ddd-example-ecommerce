TRUNCATE TABLE deliveries;
TRUNCATE TABLE delivery_items;

INSERT INTO deliveries VALUES
    ('301', '3001', 'Person 1', 'Place 1', 'READY'),
    ('302', '3002', 'Person 2', 'Place 2', 'DISPATCHED');

INSERT INTO delivery_items VALUES
    ('test-1', '111', '301'),
    ('test-2', '222', '302');