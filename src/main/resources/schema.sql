-- Drop tables in correct order
DROP TABLE IF EXISTS "orders";
DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "drinks";

-- Create users table
CREATE TABLE IF NOT EXISTS "users" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

-- Create drinks table
CREATE TABLE IF NOT EXISTS "drinks" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0
);

-- Create orders table
CREATE TABLE IF NOT EXISTS "orders" (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    drink_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    order_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "users"(id),
    FOREIGN KEY (drink_id) REFERENCES "drinks"(id)
);
