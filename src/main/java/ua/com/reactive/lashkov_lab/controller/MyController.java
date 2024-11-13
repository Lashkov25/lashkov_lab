package ua.com.reactive.lashkov_lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ua.com.reactive.lashkov_lab.handler.UserHandler;
import ua.com.reactive.lashkov_lab.handler.DrinkHandler;
import ua.com.reactive.lashkov_lab.handler.AdminHandler;

@RestController
@RequestMapping("/api")
public class MyController {

    private final UserHandler userHandler;
    private final DrinkHandler drinkHandler;
    private final AdminHandler adminHandler;

    @Autowired
    public MyController(UserHandler userHandler, DrinkHandler drinkHandler, AdminHandler adminHandler) {
        this.userHandler = userHandler;
        this.drinkHandler = drinkHandler;
        this.adminHandler = adminHandler;
    }

    // для отримання балансу користувача
    @GetMapping(value = "/user/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getUserBalance() {
        return userHandler.getUserBalance();
    }

    // для оновлення балансу
    @PostMapping(value = "/user/updateBalance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> updateUserBalance(@RequestParam("сума") Double amount) {
        return userHandler.updateUserBalance(amount);
    }

    // для отримання списку напоїв
    @GetMapping(value = "/drinks", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getAllDrinks() {
        return drinkHandler.getAllDrinks();
    }

    //для отримання інгредієнтів конкретного напою
    @GetMapping(value = "/drinks/{id}/ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> getIngredients(@PathVariable("id") Long drinkId) {
        return drinkHandler.getIngredients(drinkId);
    }

    // для покупки напою
    @PostMapping(value = "/drinks/purchase", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> purchaseDrink(@RequestParam("id") Long drinkId, @RequestParam("баланс") Double userBalance) {
        return drinkHandler.purchaseDrink(drinkId, userBalance);
    }

    // для поповнення запасів напою (для адміністратора)
    @PostMapping(value = "/admin/refill", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> refillDrink(@RequestParam("id") Long drinkId, @RequestParam("кількість") Integer quantity) {
        return adminHandler.refillDrink(drinkId, quantity);
    }
}
