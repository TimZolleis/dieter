package dev.elektronisch.dieter.server.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dieter_users")
public final class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    private String password;

    private String salt;

    private boolean admin;

    public UserEntity() {
    }

    public UserEntity(String name, String email, String password, String salt, boolean admin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", admin=" + admin +
                '}';
    }
}
