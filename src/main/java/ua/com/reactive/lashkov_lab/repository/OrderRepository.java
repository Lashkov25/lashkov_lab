package ua.com.reactive.lashkov_lab.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ua.com.reactive.lashkov_lab.entity.Order;

public interface OrderRepository extends R2dbcRepository<Order, Long> {
    Flux<Order> findByUserId(Long userId);
}
