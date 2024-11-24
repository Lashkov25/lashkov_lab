package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.repository.DrinkRepository;
import ua.com.reactive.lashkov_lab.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class DrinkService {
    private final DrinkRepository drinkRepository;
    private final UserRepository userRepository;

    public Flux<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    public Mono<Drink> getDrinkById(Long id) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Drink not found with id: " + id)));
    }

    public Mono<Drink> createDrink(Drink drink) {
        return validateDrink(drink)
                .then(drinkRepository.save(drink));
    }

    public Mono<Void> deleteDrink(Long id) {
        return drinkRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Drink not found with id: " + id)))
                .flatMap(drink -> drinkRepository.deleteById(id));
    }

    private Mono<Void> validateDrink(Drink drink) {
        return Mono.defer(() -> {
            if (drink.getName() == null || drink.getName().trim().isEmpty()) {
                return Mono.error(new IllegalArgumentException("Drink name cannot be empty"));
            }
            if (drink.getPrice() == null || drink.getPrice() < 0) {
                return Mono.error(new IllegalArgumentException("Drink price must be positive"));
            }
            if (drink.getPortionsAvailable() == null || drink.getPortionsAvailable() < 0) {
                return Mono.error(new IllegalArgumentException("Portions available cannot be negative"));
            }
            return Mono.empty();
        });
    }

    @Transactional
    public Mono<Drink> purchaseDrink(Long drinkId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(userRepository::findByUsername)
                .flatMap(user -> drinkRepository.findById(drinkId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Drink not found")))
                        .flatMap(drink -> {
                            if (drink.getPortionsAvailable() <= 0) {
                                return Mono.error(new RuntimeException("Drink is out of stock"));
                            }
                            if (drink.getPrice() > user.getBalance()) {
                                return Mono.error(new RuntimeException("Insufficient funds"));
                            }

                            user.setBalance(user.getBalance() - drink.getPrice());
                            drink.setPortionsAvailable(drink.getPortionsAvailable() - 1);

                            return userRepository.save(user)
                                    .then(drinkRepository.save(drink));
                        }));
    }
}
