package ua.com.reactive.lashkov_lab.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ua.com.reactive.lashkov_lab.handler.AdminHandler;
import ua.com.reactive.lashkov_lab.handler.DrinkHandler;
import ua.com.reactive.lashkov_lab.handler.UserHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CoffeeMachineRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route(GET("/api/users"), userHandler::getAllUsers)
                .andRoute(POST("/api/users/register"), userHandler::createUser)
                .andRoute(POST("/api/users/login"), userHandler::login);
    }

    @Bean
    public RouterFunction<ServerResponse> drinkRoutes(DrinkHandler drinkHandler) {
        return route(GET("/api/drinks"), drinkHandler::getAllDrinks)
                .andRoute(GET("/api/drinks/{id}"), drinkHandler::getDrinkById)
                .andRoute(GET("/api/drinks/{id}/ingredients"), drinkHandler::getIngredients)
                .andRoute(POST("/api/drinks/{id}/purchase"), drinkHandler::purchaseDrink);
    }

    @Bean
    public RouterFunction<ServerResponse> adminRoutes(AdminHandler adminHandler) {
        return route(POST("/api/admin/drinks"), adminHandler::createDrink)
                .andRoute(PUT("/api/admin/drinks/{id}"), adminHandler::updateDrink)
                .andRoute(DELETE("/api/admin/drinks/{id}"), adminHandler::deleteDrink)
                .andRoute(POST("/api/admin/drinks/{id}/refill"), adminHandler::refillDrink);
    }
}
