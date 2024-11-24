package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.service.UserService;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public Mono<ResponseEntity<ApiResponse<User>>> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMap(userService::findByUsername)
                .cast(User.class)
                .map(user -> ResponseEntity.ok(new ApiResponse<>(user)))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.badRequest()
                                .body(new ApiResponse<>(e.getMessage()))));
    }

    @PostMapping("/{id}/balance")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ApiResponse<User>>> updateBalance(
            @PathVariable Long id,
            @RequestParam Double amount) {
        return userService.updateUserBalance(id, amount)
                .map(user -> ResponseEntity.ok(new ApiResponse<>(user)))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.badRequest()
                                .body(new ApiResponse<>(e.getMessage()))));
    }
}
