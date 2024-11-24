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

    public Flux<Drink> findAll() {
        return drinkRepository.findAll();
    }

    public Mono<Drink> findById(Long id) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)));
    }

    public Mono<Drink> createDrink(Drink drink) {
        return drinkRepository.save(drink);
    }

    public Mono<Drink> updateDrink(Long id, Drink drink) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)))
                .flatMap(existingDrink -> {
                    existingDrink.setName(drink.getName());
                    existingDrink.setDescription(drink.getDescription());
                    existingDrink.setPrice(drink.getPrice());
                    existingDrink.setQuantity(drink.getQuantity());
                    return drinkRepository.save(existingDrink);
                });
    }

    public Mono<Void> deleteDrink(Long id) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)))
                .flatMap(drinkRepository::delete);
    }

    public Mono<Drink> updateDrinkQuantity(Long id, int quantityChange) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.DRINK_NOT_FOUND)))
                .flatMap(drink -> {
                    int newQuantity = drink.getQuantity() + quantityChange;
                    if (newQuantity < 0) {
                        return Mono.error(new BusinessException(ErrorMessages.DRINK_OUT_OF_STOCK));
                    }
                    drink.setQuantity(newQuantity);
                    return drinkRepository.save(drink);
                });
    }
}
