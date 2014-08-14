package org.ozsoft.projectbase.repositories;

import javax.ejb.Stateless;

import org.ozsoft.projectbase.entities.User;

@Stateless
public class UserRepository extends Repository<User> {

    public UserRepository() {
        super(User.class);
    }
}
