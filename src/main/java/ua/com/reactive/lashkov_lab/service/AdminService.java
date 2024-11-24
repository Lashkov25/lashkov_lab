package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.repository.DrinkRepository;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final DrinkRepository drinkRepository;

    public Mono<Drink> createDrink(Drink drink) {
        return drinkRepository.save(drink);
    }

    public Mono<Drink> updateDrink(Drink drink) {
        return drinkRepository.findById(drink.getId())
                .flatMap(existingDrink -> {
                    existingDrink.setName(drink.getName());
                    existingDrink.setPrice(drink.getPrice());
                    existingDrink.setPortionsAvailable(drink.getPortionsAvailable());
                    existingDrink.setIngredients(drink.getIngredients());
                    return drinkRepository.save(existingDrink);
                });
    }

    public Mono<Void> deleteDrink(Long id) {
        return drinkRepository.deleteById(id);
    }

    public Mono<Drink> refillDrink(Long drinkId, Integer quantity) {
        return drinkRepository.findById(drinkId)
                .flatMap(drink -> {
                    drink.setPortionsAvailable(drink.getPortionsAvailable() + quantity);
                    return drinkRepository.save(drink);
                });
    }
}
