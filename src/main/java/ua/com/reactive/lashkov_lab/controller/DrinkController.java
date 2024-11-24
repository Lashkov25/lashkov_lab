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
        return drinkService.getAllDrinks();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Drink>> getDrinkById(@PathVariable Long id) {
        return drinkService.getDrinkById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> createDrink(@RequestBody Drink drink) {
        return drinkService.createDrink(drink)
                .map(created -> ResponseEntity.ok(new ApiResponse<>(created)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> updateDrink(@PathVariable Long id, @RequestBody Drink drink) {
        return drinkService.updateDrinkInfo(id, drink)
                .map(updated -> ResponseEntity.ok(new ApiResponse<>(updated)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> deleteDrink(@PathVariable Long id) {
        return drinkService.deleteDrink(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }

    @PostMapping("/{id}/purchase")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> purchaseDrink(@PathVariable Long id) {
        return drinkService.processPurchase(id)
                .map(drink -> ResponseEntity.ok(new ApiResponse<>(drink)));
    }

    @PostMapping("/{id}/refill")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Drink>>> refillDrink(@PathVariable Long id, @RequestParam int quantity) {
        return drinkService.refillIngredients(id, quantity)
                .map(drink -> ResponseEntity.ok(new ApiResponse<>(drink)));
    }
}
