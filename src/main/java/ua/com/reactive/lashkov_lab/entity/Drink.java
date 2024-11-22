package ua.com.reactive.lashkov_lab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("drink")
public class Drink {
    @Id
    private Long id;
    private String name;
    private Double price;
    private int portionsAvailable;
    private List<Ingredient> ingredients; // Список інгредієнтів
}

