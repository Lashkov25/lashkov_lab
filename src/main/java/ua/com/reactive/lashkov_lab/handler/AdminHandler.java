package ua.com.reactive.lashkov_lab.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.entity.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AdminHandler {

    private final List<Drink> drinks;

    public AdminHandler() {
        // Ініціалізація списку напоїв з інгредієнтами
        this.drinks = new ArrayList<>(List.of(
                new Drink(1L, "Еспресо", 35.5, 14, List.of(
                        new Ingredient(1L, "Сироп", 7.0),
                        new Ingredient(2L, "Маршмеллоу", 7.0),
                        new Ingredient(3L, "Вода", 0.5)
                )),
                new Drink(2L, "Лате", 55.0,15 , List.of(
                        new Ingredient(1L, "Безлактозне молоко", 15.0),
                        new Ingredient(2L, "Маршмеллоу", 7.0),
                        new Ingredient(3L, "Сироп", 7.0)

                )),
                new Drink(3L, "Капучино", 55.0, 12, List.of(
                        new Ingredient(1L, "Безлактозне молоко", 15.0),
                        new Ingredient(2L, "Маршмеллоу", 7.0),
                        new Ingredient(3L, "Сироп", 7.0)
                ))
        ));
    }

    // Метод для поповнення запасів напою
    public Mono<ServerResponse> refillDrink(Long drinkId, Integer quantity) {
        Optional<Drink> optionalDrink = drinks.stream()
                .filter(d -> d.getId().equals(drinkId))
                .findFirst();

        if (optionalDrink.isPresent()) {
            Drink drink = optionalDrink.get();
            drink.setPortionsAvailable(drink.getPortionsAvailable() + quantity);
            return ServerResponse.ok().bodyValue("Наповнено " + drink.getName() + " " + quantity + " позицією(-ями)");
        } else {
            return ServerResponse.badRequest().bodyValue("Напій не знайдено за ID: " + drinkId);
        }
    }
}

