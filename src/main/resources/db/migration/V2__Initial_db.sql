-- Таблиця для клієнтів
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       balance NUMERIC(12,2) NOT NULL
);

-- Таблиця для напоїв
CREATE TABLE drinks (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        price NUMERIC(12,2) NOT NULL,
                        portions_available INT NOT NULL
);

-- Таблиця для інгредієнтів
CREATE TABLE ingredients (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             price NUMERIC(12,2) NOT NULL

);

-- Зв'язок між напоями та інгредієнтами
CREATE TABLE drink_ingredients (
                                   drink_id BIGINT NOT NULL,
                                   ingredient_id BIGINT NOT NULL,
                                   PRIMARY KEY (drink_id, ingredient_id),
                                   FOREIGN KEY (drink_id) REFERENCES drinks (id) ON DELETE CASCADE,
                                   FOREIGN KEY (ingredient_id) REFERENCES ingredients (id) ON DELETE CASCADE
);

-- Добавленння user
INSERT INTO users (name, balance) VALUES
                                      ('Вася Пупкін', 250.0),
                                      ('Євгеній Петренко', 1000.0);
-- Добавлення напоїів
INSERT INTO drinks (name, price, portions_available) VALUES
                                                         ('Еспресо', 35.5, 14),
                                                         ('Лате', 55.0, 15),
                                                         ('Капучино', 55.0, 12);

-- Добавлення інгредієнтів
INSERT INTO ingredients (name, price) VALUES
                                          ('Сироп', 7.0),
                                          ('Маршмеллоу', 7.0),
                                          ('Безлактозне молоко', 15.0),
                                          ('Вода', 0.5);

-- Зв'язок між напоями та інгредієнтами
INSERT INTO drink_ingredients (drink_id, ingredient_id) VALUES
                                                            (1, 1), (1, 2), (1, 4), -- Еспресо
                                                            (2, 3), (2, 2), (2, 1), -- Лате
                                                            (3, 3), (3, 2), (3, 1); -- Капучино
