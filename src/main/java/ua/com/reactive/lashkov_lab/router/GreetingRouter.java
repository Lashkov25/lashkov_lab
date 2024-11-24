package ua.com.reactive.lashkov_lab.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ua.com.reactive.lashkov_lab.handler.GreetingHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

    @Bean("greetingRoutes")
    public RouterFunction<ServerResponse> greetingRoutes(GreetingHandler greetingHandler) {
        return route(GET("/"), greetingHandler::hello)
                .andRoute(GET("/login"), greetingHandler::login)
                .andRoute(GET("/registration"), greetingHandler::registration)
                .andRoute(GET("/users").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::users)
                .andRoute(GET("/admin").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::admin);
    }
}
