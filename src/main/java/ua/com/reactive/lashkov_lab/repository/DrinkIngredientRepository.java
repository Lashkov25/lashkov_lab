package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.com.reactive.lashkov_lab.entity.DrinkIngredient;

public interface DrinkIngredientRepository extends ReactiveCrudRepository<DrinkIngredient, Long> {
    Flux<DrinkIngredient> findByDrinkId(Long drinkId);
}
