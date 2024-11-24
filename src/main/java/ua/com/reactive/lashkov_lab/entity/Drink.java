package ua.com.reactive.lashkov_lab.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Table("drinks")
public class Drink {
    @Id
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
}
