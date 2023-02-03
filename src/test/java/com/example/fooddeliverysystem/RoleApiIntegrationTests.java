package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.exceptions.role.RoleAlreadyExistsException;
import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.model.Role;
import com.example.fooddeliverysystem.repo.RoleRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleApiIntegrationTests {

    @MockBean
    private RoleRepo roleRepo;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/role without being 
            authorized, then we expect a 401 Unauthorized response status. 
            """)
    public void checkAuthRoleTest() throws Exception {

        mockMvc.perform(get("/api/v1/role"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/role with valid credentials for
            authorization, then we expect a list with all the roles from the db.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getAllRolesTest() throws Exception {

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        Role admin = new Role();
        admin.setRoleId(Long.valueOf(2));
        admin.setName("ADMIN");

        List<Role> roleList = List.of(user, admin);
        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(roleList);

        when(roleRepo.findAll()).thenReturn(roleList);

        mockMvc.perform(get("/api/v1/role")).andExpect(
                content().json(responseJson)
        );
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/role with valid credentials, but without valid authorities,
            we expect to get a 403 FORBIDDEN status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addRoleWithoutAuthorities() throws Exception {

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(user);

        when(roleRepo.searchRoleByName(user.getName())).thenReturn(Optional.of(new Role()));

        mockMvc.perform(post("/api/v1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/role with valid credentials, valid authorities,
            and a role that already exists, we expect to get a RoleAlreadyExistsException and FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingRole() throws Exception {

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(user);

        when(roleRepo.searchRoleByName(user.getName())).thenReturn(Optional.of(new Role()));

        mockMvc.perform(post("/api/v1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RoleAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/role with valid credentials, valid authorities,
            and valid role, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addRole() throws Exception {

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(user);

        when(roleRepo.searchRoleByName(user.getName())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/role/id/{id} with valid credentials, valid authorities,
            and a valid role id, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void deleteRoleById() throws Exception {

        Long roleId = Long.valueOf(1);

        mockMvc.perform(delete("/api/v1/role/id/" + roleId))
                .andExpect(status().isOk());
    }

}
