package com.root.repository;

import com.root.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByToken(String token);
    User findByUsernameAndPassword(String username, String password);
    List<User> findByUsername(String username);
}