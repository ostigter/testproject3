package org.ozsoft.toyshop.core.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = 8832860575115199429L;

    @Basic
    private String username;

    @Basic
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

}
