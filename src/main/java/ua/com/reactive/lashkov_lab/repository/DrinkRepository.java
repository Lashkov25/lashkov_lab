package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.com.reactive.lashkov_lab.entity.Drink;

public interface DrinkRepository extends ReactiveCrudRepository<Drink, Long> {
}
