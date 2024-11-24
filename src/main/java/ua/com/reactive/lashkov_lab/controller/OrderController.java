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
            Authentication authentication,
            @RequestParam Long drinkId,
            @RequestParam(defaultValue = "1") int quantity) {
        Long userId = Long.parseLong(authentication.getName());
        return orderService.createOrder(userId, drinkId, quantity)
                .map(order -> ResponseEntity.ok(ApiResponse.success(order)));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Flux<Order> getUserOrders(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return orderService.findAllByUserId(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ApiResponse<Order>>> getOrderById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = Long.parseLong(authentication.getName());
        return orderService.findByIdAndUserId(id, userId)
                .map(order -> ResponseEntity.ok(ApiResponse.success(order)));
    }
}
