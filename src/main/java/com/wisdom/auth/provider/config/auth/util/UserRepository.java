package com.wisdom.auth.provider.config.auth.util;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {

    private static final Map<String, User> userMap = new HashMap<>();

    public User findByUsername(final String username){
        return userMap.get(username);
    }

    public User insert(User user){
        userMap.put(user.getUsername(),user);
        return user;
    }

    public void remove(String username){
        userMap.remove(username);
    }
}