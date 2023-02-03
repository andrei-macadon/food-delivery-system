package com.example.fooddeliverysystem.controller;

import com.example.fooddeliverysystem.model.Role;
import com.example.fooddeliverysystem.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok()
                .body(roles);
    }

    @PostMapping
    public ResponseEntity<String> addRole(@RequestBody @Valid Role role) {
        roleService.addRole(role);
        return ResponseEntity.ok().body("The role " + role.getName() + " has been successfully saved in the db.");
    }

    @DeleteMapping("/id/{role_id}")
    public ResponseEntity<String> deleteRole(@PathVariable("role_id") Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok().body("The role with the id " + roleId + " has been deleted.");
    }


}
