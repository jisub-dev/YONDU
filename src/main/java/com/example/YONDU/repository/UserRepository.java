package com.example.YONDU.repository;

import com.example.YONDU.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentifier(String identifier);
}
