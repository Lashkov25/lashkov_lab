package ua.com.reactive.lashkov_lab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("roles_has_users")
public class RolesHasUsers {
    @Id
    private Long id;
    private Long userId;
    private Long roleId;
}