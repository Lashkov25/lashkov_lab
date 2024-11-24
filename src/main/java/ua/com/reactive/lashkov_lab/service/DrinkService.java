package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.exception.BusinessException;
import ua.com.reactive.lashkov_lab.exception.ErrorMessages;
import ua.com.reactive.lashkov_lab.repository.DrinkRepository;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final DrinkRepository drinkRepository;

    public Flux<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    public Mono<Drink> getDrinkById(Long id) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)));
    }

    public Mono<Drink> createDrink(Drink drink) {
        return drinkRepository.save(drink);
    }

    public Mono<Drink> updateDrinkInfo(Long id, Drink updatedDrink) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)))
                .flatMap(existingDrink -> {
                    existingDrink.setName(updatedDrink.getName());
                    existingDrink.setPrice(updatedDrink.getPrice());
                    existingDrink.setDescription(updatedDrink.getDescription());
                    existingDrink.setQuantity(updatedDrink.getQuantity());
                    return drinkRepository.save(existingDrink);
                });
    }

    public Mono<Void> deleteDrink(Long id) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)))
                .then(drinkRepository.deleteById(id));
    }

    public Mono<Drink> processPurchase(Long drinkId) {
        return drinkRepository.findById(drinkId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)))
                .flatMap(drink -> {
                    if (drink.getQuantity() <= 0) {
                        return Mono.error(new BusinessException(ErrorMessages.DRINK_OUT_OF_STOCK));
                    }
                    drink.setQuantity(drink.getQuantity() - 1);
                    return drinkRepository.save(drink);
                });
    }

    public Mono<Drink> refillIngredients(Long id, int quantity) {
        if (quantity <= 0) {
            return Mono.error(new BusinessException("Quantity must be positive"));
        }
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)))
                .flatMap(drink -> {
                    drink.setQuantity(drink.getQuantity() + quantity);
                    return drinkRepository.save(drink);
                });
    }
}
