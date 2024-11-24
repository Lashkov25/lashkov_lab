package ua.com.reactive.lashkov_lab.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.UserRegistrationDto;
import ua.com.reactive.lashkov_lab.service.UserService;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class GreetingHandler {
    private final UserService userService;

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_PLAIN)
                .bodyValue("Hello, this is Coffee Machine API!");
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_PLAIN)
                .bodyValue("This is login page");
    }

    public Mono<ServerResponse> registration(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_PLAIN)
                .bodyValue("This is registration page");
    }

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRegistrationDto.class)
                .flatMap(userService::registerUser)
                .flatMap(user -> ok().bodyValue(user));
    }

    public Mono<ServerResponse> users(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_PLAIN)
                .bodyValue("This is users page");
    }

    public Mono<ServerResponse> admin(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_PLAIN)
                .bodyValue("This is admin page");
    }
}
