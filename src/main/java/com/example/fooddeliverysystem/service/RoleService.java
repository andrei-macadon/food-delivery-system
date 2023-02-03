package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.model.Role;

import java.util.List;

public interface RoleService {
    void addRole(Role role);

    List<Role> getAllRoles();

    void deleteRole(Long roleId);
}
