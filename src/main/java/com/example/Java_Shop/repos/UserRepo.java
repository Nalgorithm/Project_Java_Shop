package com.example.Java_Shop.repos;

import com.example.Java_Shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String name);
}
