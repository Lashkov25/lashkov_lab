package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.service.UserService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ApiResponse<User>>> getCurrentUser(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return userService.findById(userId)
                .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ApiResponse<User>>> updateBalance(
            Authentication authentication,
            @RequestParam BigDecimal amount) {
        Long userId = Long.parseLong(authentication.getName());
        return userService.updateUserBalance(userId, amount)
                .map(user -> ResponseEntity.ok(ApiResponse.success(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
