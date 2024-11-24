package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.RolesHasUsers;

public interface RolesHasUsersRepository extends R2dbcRepository<RolesHasUsers, Long> {
    @Query("SELECT r.name FROM roles r INNER JOIN roles_has_users rhu ON r.id = rhu.role_id WHERE rhu.user_id = :userId")
    Flux<String> findRolesByUserId(Long userId);

    @Query("INSERT INTO roles_has_users (user_id, role_id) SELECT :userId, id FROM roles WHERE name = 'ROLE_USER' RETURNING user_id")
    Mono<Long> assignDefaultRole(Long userId);
}
