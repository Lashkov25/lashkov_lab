package ua.com.reactive.lashkov_lab.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ua.com.reactive.lashkov_lab.handler.GreetingHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CoffeeMachineRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(GreetingHandler greetingHandler) {
        return route(GET("/"), greetingHandler::hello)
                .andRoute(GET("/login"), greetingHandler::login)
                .andRoute(GET("/registration"), greetingHandler::registration)
                .andRoute(POST("/register"), greetingHandler::registerUser)
                .andRoute(GET("/user"), greetingHandler::users)
                .andRoute(GET("/admin"), greetingHandler::admin);
    }
}
