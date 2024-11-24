package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findByUsername(String username);
}
