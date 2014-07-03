package org.example.javaee.webapp.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.example.javaee.webapp.domain.User;

@Entity()
@Table(name = "USERS")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1409849168738151111L;

    @Id
    private String username;

    @Basic
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toDomain() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
