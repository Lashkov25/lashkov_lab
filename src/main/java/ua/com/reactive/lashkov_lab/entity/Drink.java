package ua.com.reactive.lashkov_lab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drink {
    private Long id;
    private String name;
    private Double price;
    private int portionsAvailable;
    private List<Ingredient> ingredients; // Список інгредієнтів
}
