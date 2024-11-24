package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.entity.Ingredient;
import ua.com.reactive.lashkov_lab.service.DrinkService;

import java.util.List;

@RestController
@RequestMapping("/api/drinks")
@RequiredArgsConstructor
public class DrinkController {
    private final DrinkService drinkService;

    // Отримання всіх напоїв
    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<Drink>>>> getAllDrinks() {
        return drinkService.getAllDrinks()
                .collectList()
                .map(drinks -> ResponseEntity.ok(ApiResponse.success("Drinks retrieved successfully", drinks)));
    }

    // Отримання напою за ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Drink>>> getDrinkById(@PathVariable Long id) {
        return drinkService.getDrinkById(id)
                .map(drink -> ResponseEntity.ok(ApiResponse.success("Drink found", drink)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound()
                        .build()));
    }

    // Отримання інгредієнтів напою
    @GetMapping("/{id}/ingredients")
    public Mono<ResponseEntity<ApiResponse<List<Ingredient>>>> getIngredients(@PathVariable Long id) {
        return drinkService.getDrinkById(id)
                .map(drink -> ResponseEntity.ok(ApiResponse.success("Ingredients retrieved successfully", drink.getIngredients())))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound()
                        .build()));
    }

    // Покупка напою
    @PostMapping("/{id}/purchase")
    public Mono<ResponseEntity<ApiResponse<Drink>>> purchaseDrink(@PathVariable Long id) {
        return drinkService.purchaseDrink(id)
                .map(drink -> ResponseEntity.ok(ApiResponse.success("Purchase successful", drink)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(ApiResponse.error(e.getMessage()))));
    }

    // Створення нового напою
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> createDrink(@RequestBody Drink drink) {
        return drinkService.createDrink(drink)
                .map(savedDrink -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Drink created successfully", savedDrink)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(ApiResponse.error(e.getMessage()))));
    }

    // Оновлення напою
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> updateDrink(@PathVariable Long id, @RequestBody Drink drink) {
        drink.setId(id);
        return drinkService.getDrinkById(id)
                .flatMap(existingDrink -> drinkService.createDrink(drink))
                .map(updatedDrink -> ResponseEntity.ok(ApiResponse.success("Drink updated successfully", updatedDrink)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound()
                        .build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(ApiResponse.error(e.getMessage()))));
    }

    // Видалення напою за ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteDrink(@PathVariable Long id) {
        return drinkService.getDrinkById(id)
                .flatMap(drink -> drinkService.deleteDrink(id)
                        .thenReturn(ResponseEntity.ok(ApiResponse.<Void>success("Drink deleted successfully", null))))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound()
                        .build()));
    }

    // Обробка винятків
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred: " + e.getMessage()));
    }
}
