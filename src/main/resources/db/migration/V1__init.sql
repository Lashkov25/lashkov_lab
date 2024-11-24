
-- Створення таблиць
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) DEFAULT 0.0
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE roles_has_users (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE(user_id, role_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    quantity INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(20) NOT NULL
);

CREATE TABLE drinks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT
);

CREATE TABLE drinks_ingredients (
    drink_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (drink_id, ingredient_id),
    FOREIGN KEY (drink_id) REFERENCES drinks(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    drink_id BIGINT NOT NULL REFERENCES drinks(id),
    quantity INTEGER NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Додавання базових ролей
INSERT INTO roles (name) VALUES
    ('ROLE_User'),
    ('ROLE_Admin')
ON CONFLICT (name) DO NOTHING;

-- Додавання тестових користувачів
INSERT INTO users (username, password, name, balance) VALUES
    ('1111', '1111', 'Test User 1', 100.0),
    ('2222', '2222', 'Test User 2', 100.0);

-- Призначення ролей користувачам
INSERT INTO roles_has_users (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE (u.username = '1111' AND r.name = 'ROLE_User')
   OR (u.username = '1111' AND r.name = 'ROLE_Admin')
   OR (u.username = '2222' AND r.name = 'ROLE_User');

-- Додавання тестових інгредієнтів
INSERT INTO ingredients (name, quantity, unit) VALUES
    ('Coffee beans', 1000, 'g'),
    ('Milk', 2000, 'ml'),
    ('Sugar', 1000, 'g'),
    ('Water', 5000, 'ml');

-- Додавання тестових напоїв
INSERT INTO drinks (name, price, description) VALUES
    ('Espresso', 2.50, 'Strong black coffee'),
    ('Cappuccino', 3.50, 'Espresso with steamed milk and foam'),
    ('Latte', 3.00, 'Espresso with lots of steamed milk');

-- Додавання інгредієнтів до напоїв
INSERT INTO drinks_ingredients (drink_id, ingredient_id, quantity) VALUES
    (1, 1, 7),  -- Espresso: 7g coffee beans
    (1, 4, 30), -- Espresso: 30ml water
    (2, 1, 7),  -- Cappuccino: 7g coffee beans
    (2, 2, 100),-- Cappuccino: 100ml milk
    (2, 4, 30), -- Cappuccino: 30ml water
    (3, 1, 7),  -- Latte: 7g coffee beans
    (3, 2, 150),-- Latte: 150ml milk
    (3, 4, 30); -- Latte: 30ml water