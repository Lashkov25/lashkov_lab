package ua.com.reactive.lashkov_lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.entity.User;
import ua.com.reactive.lashkov_lab.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public Mono<ResponseEntity<User>> register(@RequestBody UserRegistrationDto registrationDto) {
        return userService.registerUser(registrationDto)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
