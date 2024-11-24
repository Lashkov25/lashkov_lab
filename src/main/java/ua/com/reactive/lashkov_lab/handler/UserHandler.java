package ua.com.reactive.lashkov_lab.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.dto.AuthRequestDto;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.service.UserService;
import ua.com.reactive.lashkov_lab.security.JwtUtil;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private Mono<ServerResponse> createResponse(ApiResponse<?> response, int status) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

    private Mono<ServerResponse> createErrorResponse(String message) {
        return createResponse(
                ApiResponse.error(message),
                400
        );
    }

    private Mono<ServerResponse> createSuccessResponse(ApiResponse<?> response) {
        return createResponse(response, 200);
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return userService.getAllUsers()
                .collectList()
                .map(users -> ApiResponse.success("Users retrieved successfully", users))
                .flatMap(this::createSuccessResponse)
                .doOnSubscribe(subscription -> request.exchange().getRequest().getHeaders());
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(UserRegistrationDto.class)
                .flatMap(userService::createUser)
                .map(user -> ApiResponse.success("User registered successfully", user))
                .flatMap(this::createSuccessResponse)
                .onErrorResume(e -> createErrorResponse(e.getMessage()));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthRequestDto.class)
                .flatMap(userService::authenticateUser)
                .map(user -> {
                    String token = jwtUtil.generateToken(user);
                    return ApiResponse.success("Login successful", token);
                })
                .flatMap(this::createSuccessResponse)
                .onErrorResume(e -> createErrorResponse(e.getMessage()));
    }
}