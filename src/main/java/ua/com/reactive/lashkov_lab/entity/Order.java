package ua.com.reactive.lashkov_lab.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table("orders")
public class Order {
    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("drink_id")
    private Long drinkId;

    @Column("quantity")
    private int quantity;

    @Column("total_price")
    private BigDecimal totalPrice;

    @Column("order_date")
    private LocalDateTime orderDate;
}
