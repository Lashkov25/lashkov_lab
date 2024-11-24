package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Order;
import ua.com.reactive.lashkov_lab.repository.OrderRepository;
import ua.com.reactive.lashkov_lab.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DrinkService drinkService;

    public Mono<Order> createOrder(Order order, String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    order.setUserId(user.getId());
                    order.setStatus("PENDING");
                    order.setCreatedAt(LocalDateTime.now());
                    order.setUpdatedAt(LocalDateTime.now());
                    return drinkService.getDrinkById(order.getDrinkId())
                            .map(drink -> {
                                order.setTotalPrice(drink.getPrice() * order.getQuantity());
                                return order;
                            });
                })
                .flatMap(orderRepository::save);
    }

    public Flux<Order> getAllOrdersByUser(String username) {
        return userRepository.findByUsername(username)
                .flatMapMany(user -> orderRepository.findByUserId(user.getId()));
    }

    public Mono<Order> getOrderById(Long id, String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> orderRepository.findById(id)
                        .filter(order -> order.getUserId().equals(user.getId())));
    }

    public Mono<Order> cancelOrder(Long id, String username) {
        return getOrderById(id, username)
                .filter(order -> "PENDING".equals(order.getStatus()))
                .flatMap(order -> {
                    order.setStatus("CANCELLED");
                    order.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.save(order);
                });
    }
}
