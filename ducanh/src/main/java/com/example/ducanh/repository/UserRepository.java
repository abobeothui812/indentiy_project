package com.example.ducanh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ducanh.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
}
