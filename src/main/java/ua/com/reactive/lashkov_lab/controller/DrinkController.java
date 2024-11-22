package ua.com.reactive.lashkov_lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.service.DrinkService;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    // Отримання всіх напоїв
    @GetMapping
    public Flux<Drink> getAllDrinks() {
        return drinkService.getAllDrinks();
    }

    // Отримання напою за ID
    @GetMapping("/{id}")
    public Mono<Drink> getDrinkById(@PathVariable Long id) {
        return drinkService.getDrinkById(id);
    }

    // Створення нового напою
    @PostMapping
    public Mono<Drink> createDrink(@RequestBody Drink drink) {
        return drinkService.createDrink(drink);
    }

    // Видалення напою за ID
    @DeleteMapping("/{id}")
    public Mono<Void> deleteDrink(@PathVariable Long id) {
        return drinkService.deleteDrink(id);
    }
}
