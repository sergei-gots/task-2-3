CREATE TABLE table_i
(
    customer_id VARCHAR(200) NOT NULL PRIMARY KEY REFERENCES customer (customer_id),
    col_1 INTEGER,
    col_2 INTEGER,
    col_3 INTEGER,
    col_4 INTEGER,
    col_5 INTEGER
);