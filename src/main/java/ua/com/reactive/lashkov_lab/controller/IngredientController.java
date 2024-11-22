package ua.com.reactive.lashkov_lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Ingredient;
import ua.com.reactive.lashkov_lab.service.IngredientService;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // Отримання всіх інгредієнтів
    @GetMapping
    public Flux<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    // Отримання інгредієнта за ID
    @GetMapping("/{id}")
    public Mono<Ingredient> getIngredientById(@PathVariable Long id) {
        return ingredientService.getIngredientById(id);
    }

    // Створення нового інгредієнта
    @PostMapping
    public Mono<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.createIngredient(ingredient);
    }

    // Видалення інгредієнта за ID
    @DeleteMapping("/{id}")
    public Mono<Void> deleteIngredient(@PathVariable Long id) {
        return ingredientService.deleteIngredient(id);
    }
}
