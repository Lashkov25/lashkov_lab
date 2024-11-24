package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query("SELECT * FROM users WHERE username = :username")
    Mono<User> findByUsername(String username);
}
