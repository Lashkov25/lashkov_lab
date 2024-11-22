package ua.com.reactive.lashkov_lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.repository.DrinkRepository;

@Service
public class DrinkService {

    private final DrinkRepository drinkRepository;

    @Autowired
    public DrinkService(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    public Flux<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    public Mono<Drink> getDrinkById(Long id) {
        return drinkRepository.findById(id);
    }

    public Mono<Drink> createDrink(Drink drink) {
        return drinkRepository.save(drink);
    }

    public Mono<Void> deleteDrink(Long id) {
        return drinkRepository.deleteById(id);
    }
}
