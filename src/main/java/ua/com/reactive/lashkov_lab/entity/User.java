package ua.com.reactive.lashkov_lab.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
    private final Long id;
    private final String name;
    @Setter
    private Double balance;

    public User(Long id, String name, Double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

}


