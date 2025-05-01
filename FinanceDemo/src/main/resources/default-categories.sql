INSERT INTO Category (name, type)
VALUES ('Rent', 'EXPENSE'),
       ('Groceries', 'EXPENSE'),
       ('Utilities', 'EXPENSE'),
       ('Transportation', 'EXPENSE'),
       ('Salary', 'INCOME'),
       ('Investments', 'INCOME')
    ON CONFLICT DO NOTHING;