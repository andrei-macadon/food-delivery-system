package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.exceptions.customer.CustomerAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.customer.CustomerNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.customer.CustomerNotFoundByNameException;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemNotFoundByIdException;
import com.example.fooddeliverysystem.model.*;
import com.example.fooddeliverysystem.repo.CustomerRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerApiIntegrationTests {

    @MockBean
    private CustomerRepo customerRepo;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/customer without being 
            authorized, then we expect a 401 Unauthorized response status. 
            """)
    public void checkAuthCustomerTest() throws Exception {

        mockMvc.perform(get("/api/v1/customer"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/customer with valid credentials for
            authorization, then we expect a list with all the customers from the db.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getAllCustomersTest() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        Customer customer1 = Customer.builder()
                .customerId(Long.valueOf(1))
                .city(city)
                .address("adr1")
                .name("name1")
                .password("parola1")
                .email("email1")
                .phone("1111111111")
                .roles(List.of(user))
                .build();

        Customer customer2 = Customer.builder()
                .customerId(Long.valueOf(2))
                .city(city)
                .address("adr2")
                .name("name2")
                .password("parola2")
                .email("email2")
                .phone("2222222222")
                .roles(List.of(user))
                .build();

        List<Customer> customers = List.of(customer1, customer2);

        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(customers);

        when(customerRepo.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customer")).andExpect(
                content().json(responseJson)
        );
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/customer/id/{id} with valid credentials and a non valid id,
            we expect to get a CustomerNotFoundByIdException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCustomerByNonValidId() throws Exception {

        Long notValidId = Long.valueOf(1);

        when(customerRepo.findById(notValidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customer/id/" + notValidId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/customer/id/{id} with valid credentials and a valid id,
            we expect to get the customer with the respective id.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCustomerById() throws Exception {

        Long validId = Long.valueOf(1);

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        Customer customer1 = Customer.builder()
                .customerId(Long.valueOf(1))
                .city(city)
                .address("adr1")
                .name("name1")
                .password("parola1")
                .email("email1")
                .phone("1111111111")
                .roles(List.of(user))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(customer1);

        when(customerRepo.findById(validId)).thenReturn(Optional.of(customer1));

        mockMvc.perform(get("/api/v1/customer/id/" + validId))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/customer/{name} with valid credentials and a non valid name,
            we expect to get a CustomerNotFoundByNameException and NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCustomerByNonValidName() throws Exception {

        String nonValidName = "user1";

        when(customerRepo.searchCustomerByName(nonValidName)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customer/" + nonValidName))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerNotFoundByNameException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/customer/{name} with valid credentials and a valid name,
            we expect to get a customer with that exact name.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCustomerByName() throws Exception {

        String validName = "user1";

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        Customer customer1 = Customer.builder()
                .customerId(Long.valueOf(1))
                .city(city)
                .address("adr1")
                .name("name1")
                .password("parola1")
                .email("email1")
                .phone("1111111111")
                .roles(List.of(user))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(customer1);

        when(customerRepo.searchCustomerByName(validName)).thenReturn(Optional.of(customer1));

        mockMvc.perform(get("/api/v1/customer/" + validName))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/customer with valid credentials but with an already
            existing customer, we expect to get a CustomerAlreadyExistsException and a FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addAlreadyExistingCustomer() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        Customer customer1 = Customer.builder()
                .customerId(Long.valueOf(1))
                .city(city)
                .address("adr1")
                .name("name1")
                .password(passwordEncoder.encode("parola1"))
                .email("email1")
                .phone("1111111111")
                .roles(List.of(user))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(customer1);

        when(customerRepo.searchCustomerByEmail(customer1.getEmail())).thenReturn(Optional.of(customer1));

        mockMvc.perform(post("/api/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/customer with valid credentials and 
            with a valid customer, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addCustomer() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Role user = new Role();
        user.setRoleId(Long.valueOf(1));
        user.setName("USER");

        Customer customer1 = Customer.builder()
                .customerId(Long.valueOf(1))
                .city(city)
                .address("adr1")
                .name("name1")
                .password(passwordEncoder.encode("parola1"))
                .email("email1")
                .phone("1111111111")
                .roles(List.of(user))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(customer1);

        when(customerRepo.searchCustomerByEmail(customer1.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/customer/id/{id} with valid credentials, but without 
            valid authorities, we expect to get a 403 FORBIDDEN status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void deleteCustomerWithoutAuthorities() throws Exception {

        Long customerId = Long.valueOf(1);

        mockMvc.perform(delete("/api/v1/customer/id/" + customerId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/customer/id/{id} with valid credentials, valid authorities,
            and a non valid customer id we expect to get a 404 NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void deleteCustomerWithNonValidId() throws Exception {

        Long customerId = Long.valueOf(1);

        when(customerRepo.findById(customerId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/customer/id/" + customerId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CustomerNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/customer/id/{id} with valid credentials, valid authorities,
            and a valid customer id we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void deleteCustomer() throws Exception {

        Long customerId = Long.valueOf(1);

        when(customerRepo.findById(customerId)).thenReturn(Optional.of(new Customer()));

        mockMvc.perform(delete("/api/v1/customer/id/" + customerId))
                .andExpect(status().isOk());
    }
}
