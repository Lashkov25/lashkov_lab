package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ua.com.reactive.lashkov_lab.entity.Role;

public interface RoleRepository extends R2dbcRepository<Role, Long> {
}
