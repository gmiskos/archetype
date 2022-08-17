package com.example.archetype.auth;

import com.example.archetype.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
