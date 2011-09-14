package org.ozsoft.projectbase.services;

import org.ozsoft.projectbase.entities.User;

public interface UserService {
    
    long create(User user);
    
    User retrieve(long id);
    
    void update(User user);
    
    void delete(User user);
    
    User authenticate(String username, String password);

}
