package org.ozsoft.projectbase.services;

import java.util.List;

import org.ozsoft.projectbase.entities.User;

public interface UserService {
    
    long create(User user);
    
    User retrieve(long id);
    
    List<User> findAll();
    
    void update(User user);
    
    void delete(User user);
    
    User authenticate(String username, String password);

}
