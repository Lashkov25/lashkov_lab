package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.Order;
import ua.com.reactive.lashkov_lab.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody Order order, Authentication authentication) {
        return orderService.createOrder(order, authentication.getName())
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<Order> getAllOrders(Authentication authentication) {
        return orderService.getAllOrdersByUser(authentication.getName());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> getOrderById(@PathVariable Long id, Authentication authentication) {
        return orderService.getOrderById(id, authentication.getName())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/cancel")
    public Mono<ResponseEntity<Order>> cancelOrder(@PathVariable Long id, Authentication authentication) {
        return orderService.cancelOrder(id, authentication.getName())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
