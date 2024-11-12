package ua.com.reactive.lashkov_lab.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ua.com.reactive.lashkov_lab.handler.AdminHandler;
import ua.com.reactive.lashkov_lab.handler.DrinkHandler;
import ua.com.reactive.lashkov_lab.handler.UserHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class CoffeeMachineRouter {

    @Bean
    public RouterFunction<ServerResponse> route(UserHandler userHandler, DrinkHandler drinkHandler, AdminHandler adminHandler) {
        return RouterFunctions
                .route(GET("/user/balance"), userHandler::getUserBalance)
                .andRoute(POST("/user/updateBalance"), userHandler::updateUserBalance)
                .andRoute(GET("/drinks"), drinkHandler::getAllDrinks)
                .andRoute(POST("/drinks/purchase"), drinkHandler::purchaseDrink)
                .andRoute(POST("/admin/refill"), adminHandler::refillDrink);
    }
}