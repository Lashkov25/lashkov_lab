package ua.com.reactive.lashkov_lab.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.dto.ApiResponse;
import ua.com.reactive.lashkov_lab.entity.Drink;
import ua.com.reactive.lashkov_lab.service.AdminService;

@Component
@RequiredArgsConstructor
public class AdminHandler {
    private final AdminService adminService;

    // Метод для створення напою
    public Mono<ServerResponse> createDrink(ServerRequest request) {
        return request.bodyToMono(Drink.class)
                .flatMap(adminService::createDrink)
                .flatMap(drink -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success("Drink created successfully", drink)))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.error(e.getMessage())));
    }

    // Метод для оновлення напою
    public Mono<ServerResponse> updateDrink(ServerRequest request) {
        Long drinkId = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(Drink.class)
                .flatMap(drink -> {
                    drink.setId(drinkId);
                    return adminService.updateDrink(drink);
                })
                .flatMap(drink -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success("Drink updated successfully", drink)))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.error(e.getMessage())));
    }

    // Метод для видалення напою
    public Mono<ServerResponse> deleteDrink(ServerRequest request) {
        Long drinkId = Long.parseLong(request.pathVariable("id"));
        return adminService.deleteDrink(drinkId)
                .then(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success("Drink deleted successfully", null)))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.error(e.getMessage())));
    }

    // Метод для поповнення запасів напою
    public Mono<ServerResponse> refillDrink(ServerRequest request) {
        Long drinkId = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(Integer.class)
                .flatMap(quantity -> adminService.refillDrink(drinkId, quantity))
                .flatMap(success -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success("Drink refilled successfully", null)))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.error(e.getMessage())));
    }
}
