package ua.com.reactive.lashkov_lab.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("drinks")
public class Drink {
    @Id
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
