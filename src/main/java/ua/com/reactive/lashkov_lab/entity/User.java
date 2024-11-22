package ua.com.reactive.lashkov_lab.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Table("users")
public class User {

    @Id
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


