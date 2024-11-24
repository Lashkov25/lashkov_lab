
-- Створення таблиці користувачів
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.0
);

-- Створення таблиці ролей
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Додавання базових ролей
INSERT INTO roles (name) VALUES
    ('ADMIN'),
    ('USER'),
    ('BARISTA'),
    ('MANAGER')
ON CONFLICT (name) DO NOTHING;

-- Створення таблиці для зв'язку користувачів з ролями
CREATE TABLE roles_has_users (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE(user_id, role_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Створення таблиці інгредієнтів
CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    quantity INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(20) NOT NULL
);

-- Створення таблиці напоїв
CREATE TABLE drinks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT
);

-- Створення таблиці для зв'язку напоїв з інгредієнтами
CREATE TABLE drinks_ingredients (
    drink_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (drink_id, ingredient_id),
    FOREIGN KEY (drink_id) REFERENCES drinks(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- Створення таблиці замовлень
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    drink_id BIGINT NOT NULL REFERENCES drinks(id),
    quantity INTEGER NOT NULL,
    total_price DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Додавання тестових користувачів
INSERT INTO users (username, password, name, balance) VALUES
    ('admin', '$2a$10$kIMlRgpErZXbSf0c1XTCcu1n0qQkY6qRtcwuRh9CRBXe0LAt3J7ga', 'Administrator', 1000.0),
    ('user', '$2a$10$kIMlRgpErZXbSf0c1XTCcu1n0qQkY6qRtcwuRh9CRBXe0LAt3J7ga', 'Test User', 500.0),
    ('barista', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Coffee Master', 0.0),
    ('manager', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Store Manager', 500.0);

-- Призначення ролей користувачам
INSERT INTO roles_has_users (user_id, role_id)
SELECT u.id, r.id
FROM users u
CROSS JOIN roles r
WHERE (u.username = 'admin' AND r.name = 'ADMIN')
   OR (u.username = 'user' AND r.name = 'USER')
   OR (u.username = 'barista' AND r.name = 'BARISTA')
   OR (u.username = 'manager' AND r.name = 'MANAGER');