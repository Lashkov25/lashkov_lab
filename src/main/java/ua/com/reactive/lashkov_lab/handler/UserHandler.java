package ua.com.reactive.lashkov_lab.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.entity.User;

@Component
public class UserHandler {

    // Імітація облікового запису користувача
    private final User user = new User(1L, "Вася Пупкін", 250.0);

    // Метод для отримання балансу користувача
    public Mono<ServerResponse> getUserBalance() {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user.getBalance());
    }

    // Метод для оновлення балансу після покупки
    public Mono<ServerResponse> updateUserBalance(Double amount) {
        if (amount > user.getBalance()) {
            return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("Недостатньо коштів");
        }

        user.setBalance(user.getBalance() - amount);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("Новий баланс: " + user.getBalance());
    }
}
