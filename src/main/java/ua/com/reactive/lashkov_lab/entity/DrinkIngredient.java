package ua.com.reactive.lashkov_lab.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("drinks_ingredients")
public class DrinkIngredient {
    @Id
    private Long id;
    private Long drinkId;
    private Long ingredientId;
    private Integer quantity;
}
