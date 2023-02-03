package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByNameException;
import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.repo.RestaurantRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantApiIntegrationTests {

    @MockBean
    private RestaurantRepo restaurantRepo;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/restaurant without being 
            authorized, then we expect a 401 Unauthorized response status. 
            """)
    public void checkAuthRestaurantTest() throws Exception {

        mockMvc.perform(get("/api/v1/restaurant"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/restaurant with valid credentials for
            authorization, then we expect a list with all the restaurants from the db.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getAllRestaurantsTest() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant1 = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .restaurantId(Long.valueOf(2))
                .name("Rest2")
                .city(city)
                .address("Adresa 2")
                .build();

        Restaurant restaurant3 = Restaurant.builder()
                .restaurantId(Long.valueOf(3))
                .name("Rest3")
                .city(city)
                .address("Adresa 3")
                .build();

        ObjectMapper mapper = new ObjectMapper();

        when(restaurantRepo.findAll()).thenReturn(List.of(restaurant1, restaurant2, restaurant3));

        mockMvc.perform(get("/api/v1/restaurant")).andExpect(
                content().json(mapper.writeValueAsString(List.of(restaurant1, restaurant2, restaurant3)))
        );
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/restaurant/{name} with valid credentials and a non valid name,
            we expect to get a RestaurantNotFoundByNameException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getRestaurantWithNonValidName() throws Exception {

        String notValidName = "rest1";

        when(restaurantRepo.searchRestaurantByName(notValidName)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/restaurant/" + notValidName))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RestaurantNotFoundByNameException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/restaurant/{name} with valid credentials and a valid name,
            we expect to get the restaurant with that name.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getRestaurantWithValidName() throws Exception {

        String restaurantName = "Rest1";

        ObjectMapper mapper = new ObjectMapper();
        when(restaurantRepo.searchRestaurantByName(restaurantName)).thenReturn(Optional.of(new Restaurant()));

        mockMvc.perform(get("/api/v1/restaurant/" + restaurantName))
                .andExpect(content().json(mapper.writeValueAsString(new Restaurant())));
    }



    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/restaurant/id/{id} with valid credentials and a non valid id,
            we expect to get a RestaurantNotFoundByIdException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getRestaurantWithNonValidId() throws Exception {

        Long notValidId = Long.valueOf(1);

        when(restaurantRepo.findById(notValidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/restaurant/id/" + notValidId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RestaurantNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/restaurant/id/{id} with valid credentials and a valid id,
            we expect to get the restaurant with that id.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getRestaurantWithValidId() throws Exception {

        Long validId = Long.valueOf(1);

        ObjectMapper mapper = new ObjectMapper();
        when(restaurantRepo.findById(validId)).thenReturn(Optional.of(new Restaurant()));

        mockMvc.perform(get("/api/v1/restaurant/id/" + validId))
                .andExpect(content().json(mapper.writeValueAsString(new Restaurant())));
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/restaurant with valid credentials but without the
            appropriate authorities, we expect to get a 403 FORBIDDEN status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addRestaurantWithoutAuthorities() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant1 = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(restaurant1);

        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/restaurant with valid credentials, valid authorities,
            and a restaurant that already exists, we expect to get a RestaurantAlreadyExistsException and a
            FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingRestaurant() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant1 = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(restaurant1);

        when(restaurantRepo.searchRestaurantByName(restaurant1.getName())).thenReturn(Optional.of(new Restaurant()));


        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RestaurantAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/restaurant with valid credentials, valid authorities,
            and a valid restaurant, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidRestaurant() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant1 = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(restaurant1);

        when(restaurantRepo.searchRestaurantByName(restaurant1.getName())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/restaurant/city_id with valid credentials, valid authorities,
            and an already exists restaurant, we expect to get a RestaurantAlreadyExistsException and 
            a FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingRestaurantToCity() throws Exception {

        Long cityId = Long.valueOf(1);

        Restaurant restaurant1 = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .address("Adresa 1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(restaurant1);

        when(restaurantRepo.searchRestaurantByName(restaurant1.getName())).thenReturn(Optional.of(restaurant1));

        mockMvc.perform(post("/api/v1/restaurant/" + cityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RestaurantAlreadyExistsException))
                .andExpect(status().isFound());
    }


    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/restaurant/{city_id} with valid credentials, valid authorities,
            and a valid restaurant, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidRestaurantToCity() throws Exception {

        Long cityId = Long.valueOf(1);

        Restaurant restaurant1 = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .address("Adresa 1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(restaurant1);

        when(restaurantRepo.searchRestaurantByName(restaurant1.getName())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/restaurant/" + cityId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/restaurant/{id} with valid credentials, valid authorities,
            and a restaurant id, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void deleteRestaurantById() throws Exception {

        Long restaurantId = Long.valueOf(1);

        mockMvc.perform(delete("/api/v1/restaurant/" + restaurantId))
                .andExpect(status().isOk());
    }

}
