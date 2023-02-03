package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.exceptions.role.RoleAlreadyExistsException;
import com.example.fooddeliverysystem.model.Role;
import com.example.fooddeliverysystem.repo.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }


    @Override
    public void addRole(Role role) {

        Optional<Role> optionalRole = roleRepo.searchRoleByName(role.getName());
        optionalRole.ifPresentOrElse(
                (x) -> {
                  throw new RoleAlreadyExistsException(x.getName());
                },
                () -> roleRepo.save(role)
        );
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepo.deleteById(roleId);
    }
}
