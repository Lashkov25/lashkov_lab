package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.service.DrinkService;

@RestController
@RequestMapping("/api/drinks")
@RequiredArgsConstructor
public class DrinkController {
    private final DrinkService drinkService;

    @GetMapping
    public Flux<Drink> getAllDrinks() {
        return drinkService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ApiResponse<Drink>>> getDrinkById(@PathVariable Long id) {
        return drinkService.findById(id)
                .map(drink -> ResponseEntity.ok(new ApiResponse<>(drink)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> createDrink(@RequestBody Drink drink) {
        return drinkService.createDrink(drink)
                .map(createdDrink -> ResponseEntity.ok(new ApiResponse<>(createdDrink)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> updateDrink(
            @PathVariable Long id,
            @RequestBody Drink drink) {
        return drinkService.updateDrink(id, drink)
                .map(updatedDrink -> ResponseEntity.ok(new ApiResponse<>(updatedDrink)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> deleteDrink(@PathVariable Long id) {
        return drinkService.deleteDrink(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
    }

    @PutMapping("/{id}/quantity")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity) {
        return drinkService.updateDrinkQuantity(id, quantity)
                .map(drink -> ResponseEntity.ok(new ApiResponse<>(drink)));
    }
}
