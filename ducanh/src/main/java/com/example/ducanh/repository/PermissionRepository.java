package com.example.ducanh.repository;

import com.example.ducanh.entity.Permissions;
import com.example.ducanh.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, String> {
    Optional<Permissions> deletePermissionsByName(String name);
}
