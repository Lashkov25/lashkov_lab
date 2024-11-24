package ua.com.reactive.lashkov_lab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("roles_has_users")
public class RolesHasUsers {
    @Column("user_id")
    private Long userId;
    
    @Column("role_id")
    private Long roleId;
}