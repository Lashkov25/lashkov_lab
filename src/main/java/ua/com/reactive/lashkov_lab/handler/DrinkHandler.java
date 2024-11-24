package ua.com.reactive.lashkov_lab.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.service.DrinkService;

/**
 * Клас обробник запитів на напої
 */
@Component
@RequiredArgsConstructor
public class DrinkHandler {
    // Сервіс роботи з напоями
    private final DrinkService drinkService;

    // Метод створення відповіді
    private Mono<ServerResponse> createResponse(ApiResponse<?> response, int status) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

    // Метод створення помилкової відповіді
    private Mono<ServerResponse> createErrorResponse(String message) {
        return createResponse(
                ApiResponse.error(message),
                400
        );
    }

    // Метод створення успішної відповіді
    private Mono<ServerResponse> createSuccessResponse(ApiResponse<?> response) {
        return createResponse(response, 200);
    }

    // Метод отримання всіх напоїв
    public Mono<ServerResponse> getAllDrinks(ServerRequest request) {
        return drinkService.getAllDrinks()
                .collectList()
                .map(drinks -> ApiResponse.success("Drinks retrieved successfully", drinks))
                .flatMap(this::createSuccessResponse)
                .doOnSubscribe(subscription -> request.attributes()); // Use request parameter
    }

    // Метод отримання напою за ідентифікатором
    public Mono<ServerResponse> getDrinkById(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMap(drinkService::getDrinkById)
                .map(drink -> ApiResponse.success("Drink found", drink))
                .flatMap(this::createSuccessResponse)
                .switchIfEmpty(createResponse(ApiResponse.error("Drink not found"), 404));
    }

    // Метод отримання інгредієнтів напою
    public Mono<ServerResponse> getIngredients(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMap(drinkService::getDrinkById)
                .map(drink -> ApiResponse.success("Ingredients retrieved successfully", drink.getIngredients()))
                .flatMap(this::createSuccessResponse)
                .switchIfEmpty(createResponse(ApiResponse.error("Drink not found"), 404));
    }

    // Метод придбання напою
    public Mono<ServerResponse> purchaseDrink(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMap(drinkService::purchaseDrink)
                .map(drink -> ApiResponse.success("Purchase successful", drink))
                .flatMap(this::createSuccessResponse)
                .onErrorResume(e -> createErrorResponse(e.getMessage()));
    }
}