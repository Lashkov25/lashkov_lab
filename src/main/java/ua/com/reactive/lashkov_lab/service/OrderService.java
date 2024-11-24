package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Order;
import ua.com.reactive.lashkov_lab.exception.BusinessException;
import ua.com.reactive.lashkov_lab.exception.ErrorMessages;
import ua.com.reactive.lashkov_lab.repository.OrderRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final DrinkService drinkService;
    private final UserService userService;

    public Mono<Order> createOrder(Long drinkId, Long userId) {
        return drinkService.getDrinkById(drinkId)
                .flatMap(drink -> userService.updateUserBalance(userId, -drink.getPrice())
                        .then(drinkService.processPurchase(drinkId))
                        .flatMap(updatedDrink -> {
                            Order order = new Order();
                            order.setDrinkId(drinkId);
                            order.setUserId(userId);
                            order.setTotalPrice(drink.getPrice());
                            order.setStatus("PENDING");
                            order.setCreatedAt(LocalDateTime.now());
                            order.setUpdatedAt(LocalDateTime.now());
                            return orderRepository.save(order);
                        }));
    }

    public Mono<Order> processOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.ORDER_NOT_FOUND)))
                .flatMap(order -> {
                    order.setStatus("COMPLETED");
                    order.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.save(order);
                });
    }

    public Flux<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Mono<Order> getOrder(Long orderId, Long userId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.ORDER_NOT_FOUND_OR_ACCESS_DENIED)));
    }
}
