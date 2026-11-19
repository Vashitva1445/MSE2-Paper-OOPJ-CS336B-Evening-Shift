CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

CREATE TABLE IF NOT EXISTS Orders (
    order_id INT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    status VARCHAR(30) NOT NULL
);

-- Optional: clear old data before a fresh demonstration.
-- DELETE FROM Orders;

SELECT * FROM Orders;
