package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.entity.Order;
import ua.com.reactive.lashkov_lab.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ApiResponse<Order>>> createOrder(
            @RequestParam Long drinkId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return orderService.createOrder(drinkId, userId)
                .map(order -> ResponseEntity.ok(new ApiResponse<>(order)));
    }

    @PostMapping("/{orderId}/process")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<Order>>> processOrder(@PathVariable Long orderId) {
        return orderService.processOrder(orderId)
                .map(order -> ResponseEntity.ok(new ApiResponse<>(order)));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Flux<Order> getUserOrders(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return orderService.getUserOrders(userId);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ApiResponse<Order>>> getOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return orderService.getOrder(orderId, userId)
                .map(order -> ResponseEntity.ok(new ApiResponse<>(order)));
    }
}
