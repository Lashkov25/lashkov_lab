package ua.com.reactive.lashkov_lab.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.entity.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DrinkHandler {

    // Змінюваний список напоїв із інгредієнтами
    private final List<Drink> drinks = new ArrayList<>(List.of(
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
    // Метод для отримання списку напоїв
    public Mono<ServerResponse> getAllDrinks() {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.fromIterable(drinks), Drink.class);
    }

    // Метод для отримання інгредієнтів конкретного напою
    public Mono<ServerResponse> getIngredients(Long drinkId) {
        Optional<Drink> optionalDrink = drinks.stream()
                .filter(d -> d.getId().equals(drinkId))
                .findFirst();

        if (optionalDrink.isPresent()) {
            List<Ingredient> ingredients = optionalDrink.get().getIngredients();
            return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Flux.fromIterable(ingredients), Ingredient.class);
        } else {
            return ServerResponse.badRequest().bodyValue("Напій не знайдено");
        }
    }

    // Метод для покупки напою
    public Mono<ServerResponse> purchaseDrink(Long drinkId, Double userBalance) {
        Optional<Drink> optionalDrink = drinks.stream()
                .filter(d -> d.getId().equals(drinkId))
                .findFirst();

        if (optionalDrink.isPresent()) {
            Drink drink = optionalDrink.get();
            if (userBalance >= drink.getPrice() && drink.getPortionsAvailable() > 0) {
                drink.setPortionsAvailable(drink.getPortionsAvailable() - 1);
                return ServerResponse.ok().bodyValue("Придбано " + drink.getName());
            } else if (userBalance < drink.getPrice()) {
                return ServerResponse.badRequest().bodyValue("Недостатньо коштів");
            } else {
                return ServerResponse.badRequest().bodyValue("Напій відсутній");
            }
        } else {
            return ServerResponse.badRequest().bodyValue("Напій не знайдено");
        }
    }
}