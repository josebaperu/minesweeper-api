package com.root.service.impl;

import com.root.entity.User;
import com.root.repository.UserRepository;
import com.root.service.UserService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public User findByToken(String token) {
        return  userRepository.findByToken(token);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
