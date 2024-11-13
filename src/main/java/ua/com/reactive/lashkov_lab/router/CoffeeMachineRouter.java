package ua.com.reactive.lashkov_lab.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ua.com.reactive.lashkov_lab.handler.AdminHandler;
import ua.com.reactive.lashkov_lab.handler.DrinkHandler;
import ua.com.reactive.lashkov_lab.handler.UserHandler;

@Configuration
public class CoffeeMachineRouter {

    @Bean
    public RouterFunction<ServerResponse> route(UserHandler userHandler, DrinkHandler drinkHandler, AdminHandler adminHandler) {
        return RouterFunctions
                // Route для отримання балансу користувача
                .route(RequestPredicates.GET("/api/user/balance"), request -> userHandler.getUserBalance())

                // Route для оновлення балансу користувача
                .andRoute(RequestPredicates.POST("/api/user/updateBalance"),
                        request -> {
                            Double amount = Double.valueOf(request.queryParam("сума").orElse("0"));
                            return userHandler.updateUserBalance(amount);
                        })

                // Route для отримання всіх напоїв
                .andRoute(RequestPredicates.GET("/api/drinks"), request -> drinkHandler.getAllDrinks())

                // Route для придбання напою
                .andRoute(RequestPredicates.POST("/api/drinks/purchase"),
                        request -> {
                            Long drinkId = Long.valueOf(request.queryParam("id").orElse("1"));
                            Double userBalance = Double.valueOf(request.queryParam("баланс").orElse("0"));
                            return drinkHandler.purchaseDrink(drinkId, userBalance);
                        })

                // Route для поповнення напою (для адміністратора)
                .andRoute(RequestPredicates.POST("/api/admin/refill"),
                        request -> {
                            Long drinkId = Long.valueOf(request.queryParam("id").orElse("1"));
                            Integer quantity = Integer.valueOf(request.queryParam("кількість").orElse("1"));
                            return adminHandler.refillDrink(drinkId, quantity);
                        })

                // Route для отримання інгредієнтів конкретного напою
                .andRoute(RequestPredicates.GET("/api/drinks/{id}/ingredients"),
                        request -> {
                            Long drinkId = Long.valueOf(request.pathVariable("id"));
                            return drinkHandler.getIngredients(drinkId);
                        });
    }
}
