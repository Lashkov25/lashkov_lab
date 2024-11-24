package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Role;

public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
    @Override
    @NonNull
    Mono<Role> findById(@NonNull Long id);

    @NonNull
    Mono<Role> findByName(@NonNull String name);
}
