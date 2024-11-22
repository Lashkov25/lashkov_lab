package ua.com.reactive.lashkov_lab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Ingredient;
import ua.com.reactive.lashkov_lab.repository.IngredientRepository;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Flux<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Mono<Ingredient> getIngredientById(Long id) {
        return ingredientRepository.findById(id);
    }

    public Mono<Ingredient> createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Mono<Void> deleteIngredient(Long id) {
        return ingredientRepository.deleteById(id);
    }
}
