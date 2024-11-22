package ua.com.reactive.lashkov_lab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("ingredient")
public class Ingredient {
    @Id
    private Long id;
    private String name;
    private Double price;
}
