package ua.com.reactive.lashkov_lab.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    private Long id;
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}