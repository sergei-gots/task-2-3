CREATE TABLE table_many (
   customer_id VARCHAR(200) NOT NULL REFERENCES customer (customer_id),
   groupId INT NOT NULL
);