package com.moonz.springbootsecurity.repository;

import com.moonz.springbootsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
