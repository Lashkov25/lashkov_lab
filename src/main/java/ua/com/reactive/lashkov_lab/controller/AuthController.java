package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.dto.AuthRequestDto;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.service.UserService;
import ua.com.reactive.lashkov_lab.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public Mono<ResponseEntity<ApiResponse<User>>> register(@RequestBody UserRegistrationDto request) {
        return userService.createUser(request)
                .map(user -> ResponseEntity.ok(ApiResponse.success("User registered successfully", user)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(ApiResponse.error(e.getMessage()))));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ApiResponse<String>>> login(@RequestBody AuthRequestDto request) {
        return userService.authenticateUser(request)
                .map(user -> ResponseEntity.ok(ApiResponse.success("Login successful", jwtUtil.generateToken(user))))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(ApiResponse.error(e.getMessage()))));
    }
}
