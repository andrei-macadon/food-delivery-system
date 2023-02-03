package com.example.fooddeliverysystem.exceptions.role;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(String roleName) {
        super("Role " + roleName + " already exists.");
    }
}
