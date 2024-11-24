package ua.com.reactive.lashkov_lab.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("drinks")
public class Drink {
    @Id
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @Builder.Default
    private String description = "";

    @NotNull(message = "Portions available is required")
    @Min(value = 0, message = "Portions available must be non-negative")
    private Integer portionsAvailable;

    @Builder.Default
    private List<Ingredient> ingredients = new ArrayList<>();
}
