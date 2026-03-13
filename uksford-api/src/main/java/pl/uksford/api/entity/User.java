package pl.uksford.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import pl.uksford.api.converter.UserRoleConverter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Size(max = 72)
    @NotNull
    @Column(name = "password", nullable = false, length = 72)
    private String password;

    @Size(max = 50)
    @NotNull
    @Column(name = "nick", nullable = false, length = 50)
    private String nick;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews = new LinkedHashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<Vote> votes = new LinkedHashSet<>();

    @Column(name = "role", nullable = false, columnDefinition = "user_role")
    @Convert(converter = UserRoleConverter.class)
    @ColumnTransformer(write = "?::user_role")
    private UserRole role;

    @NotNull
    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive = true;
}