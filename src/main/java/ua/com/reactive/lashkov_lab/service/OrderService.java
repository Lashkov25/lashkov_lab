package ua.com.reactive.lashkov_lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.entity.Order;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.exception.BusinessException;
import ua.com.reactive.lashkov_lab.exception.ErrorMessages;
import ua.com.reactive.lashkov_lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final DrinkService drinkService;

    public Mono<Order> createOrder(Long userId, Long drinkId, int quantity) {
        if (quantity <= 0) {
            return Mono.error(new BusinessException(ErrorMessages.QUANTITY_MUST_BE_POSITIVE));
        }

        return Mono.zip(
                userService.findById(userId),
                drinkService.findById(drinkId)
        ).flatMap(tuple -> {
            User user = tuple.getT1();
            Drink drink = tuple.getT2();

            BigDecimal totalPrice = drink.getPrice().multiply(BigDecimal.valueOf(quantity));

            if (user.getBalance().compareTo(totalPrice) < 0) {
                return Mono.error(new BusinessException(ErrorMessages.INSUFFICIENT_BALANCE));
            }

            if (drink.getQuantity() < quantity) {
                return Mono.error(new BusinessException(ErrorMessages.DRINK_OUT_OF_STOCK));
            }

            Order order = new Order();
            order.setUserId(userId);
            order.setDrinkId(drinkId);
            order.setQuantity(quantity);
            order.setTotalPrice(totalPrice);
            order.setOrderDate(LocalDateTime.now());

            return Mono.zip(
                    orderRepository.save(order),
                    userService.updateUserBalance(userId, totalPrice.negate()),
                    drinkService.updateDrinkQuantity(drinkId, -quantity)
            ).map(result -> result.getT1());
        });
    }

    public Flux<Order> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.ORDER_NOT_FOUND)));
    }

    public Mono<Order> findByIdAndUserId(Long id, Long userId) {
        return orderRepository.findByIdAndUserId(id, userId)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorMessages.ORDER_NOT_FOUND_OR_ACCESS_DENIED)));
    }
}
