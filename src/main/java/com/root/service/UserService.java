package com.root.service;

import com.root.entity.User;

import java.util.List;

public interface UserService {
    String generateToken();
    User findByToken(String token);
    User findByUsernameAndPassword(String username, String password);
    List<User> findByUsername(String username);
}
