-- Drop tables in correct order
DROP TABLE IF EXISTS "orders";
DROP TABLE IF EXISTS "roles_has_users";
DROP TABLE IF EXISTS "users";
DROP TABLE IF EXISTS "drinks";
DROP TABLE IF EXISTS "roles";

-- Create roles table
CREATE TABLE "roles" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Create users table
CREATE TABLE "users" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) DEFAULT 0.0,
    role_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES "roles"(id)
);

-- Create drinks table
CREATE TABLE "drinks" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT DEFAULT 0
);

-- Create orders table
CREATE TABLE "orders" (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    drink_id BIGINT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "users"(id),
    FOREIGN KEY (drink_id) REFERENCES "drinks"(id)
);

-- Insert default roles
INSERT INTO "roles" (name) VALUES ('ROLE_USER');
INSERT INTO "roles" (name) VALUES ('ROLE_ADMIN');
