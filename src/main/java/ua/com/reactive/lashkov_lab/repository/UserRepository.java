package ua.com.reactive.lashkov_lab.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ua.com.reactive.lashkov_lab.entity.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
