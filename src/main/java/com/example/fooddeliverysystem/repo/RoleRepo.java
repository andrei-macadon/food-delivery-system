package com.example.fooddeliverysystem.repo;

import com.example.fooddeliverysystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query("SELECT r from Role r where r.name = :name")
    Optional<Role> searchRoleByName(String name);
}
