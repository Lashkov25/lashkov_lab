package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.RolesHasUsers;

public interface RolesHasUsersRepository extends ReactiveCrudRepository<RolesHasUsers, Long> {
    Mono<Void> deleteAllByUserId(Long userId);
}
