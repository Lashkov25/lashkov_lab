package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.dto.AuthResponse;
import ua.com.reactive.lashkov_lab.dto.LoginDto;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public Mono<ResponseEntity<ApiResponse<AuthResponse>>> register(@RequestBody UserRegistrationDto registrationDto) {
        return authService.registerUser(registrationDto, false)
                .map(authResponse -> ResponseEntity.ok(ApiResponse.success(authResponse)));
    }

    @PostMapping("/register/admin")
    public Mono<ResponseEntity<ApiResponse<AuthResponse>>> registerAdmin(@RequestBody UserRegistrationDto registrationDto) {
        return authService.registerUser(registrationDto, true)
                .map(authResponse -> ResponseEntity.ok(ApiResponse.success(authResponse)));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ApiResponse<AuthResponse>>> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto)
                .map(authResponse -> ResponseEntity.ok(ApiResponse.success(authResponse)));
    }
}
