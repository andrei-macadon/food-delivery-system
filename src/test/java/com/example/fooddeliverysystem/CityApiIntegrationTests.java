package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.exceptions.city.CityAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.city.CityNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.city.CityNotFoundByNameException;
import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.repo.CityRepo;
import com.example.fooddeliverysystem.service.CityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CityApiIntegrationTests {

    @MockBean
    private CityRepo cityRepo;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/city without being 
            authorized, then we expect a 401 Unauthorized response status. 
            """)
    public void checkAuthCityTest() throws Exception {

        mockMvc.perform(get("/api/v1/city"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/city with valid credentials,
            then we expect a list with all the cities from the db. 
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getAllCitiesTest() throws Exception {
        City city1 = new City();
        city1.setCityId(Long.valueOf(1));
        city1.setName("Bucuresti");
        city1.setZipcode("111111");

        City city2 = new City();
        city2.setCityId(Long.valueOf(2));
        city2.setName("Timisoara");
        city2.setZipcode("222222");

        City city3 = new City();
        city3.setCityId(Long.valueOf(3));
        city3.setName("Galati");
        city3.setZipcode("333333");

        List<City> cityList = List.of(city1, city2, city3);
        ObjectMapper objectMapper = new ObjectMapper();

        when(cityRepo.findAll()).thenReturn(cityList);

        mockMvc.perform(get("/api/v1/city"))
                .andExpect(content().json(objectMapper.writeValueAsString(cityList)));
    }


    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/city/id/{id} with valid credentials
            and non valid id, we expect a CityNotFoundByIdException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCityByNonValidIdTest() throws Exception {

        Long notValidId = Long.valueOf(2);
        when(cityRepo.findById(notValidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/city/id/" + notValidId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CityNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/city/id/{id} with valid credentials
            and valid id, we expect a city with the respective id.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCityByValidIdTest() throws Exception {
        City city1 = new City();
        Long id = Long.valueOf(1);
        city1.setCityId(id);
        city1.setName("Bucuresti");
        city1.setZipcode("111111");

        ObjectMapper objectMapper = new ObjectMapper();
        when(cityRepo.findById(id)).thenReturn(Optional.of(city1));

        mockMvc.perform(get("/api/v1/city/id/" + id))
                .andExpect(content().json(objectMapper.writeValueAsString(city1)));
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/city/{name} with valid credentials
            and non valid name, we expect a CityNotFoundByNameException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCityByNonValidNameTest() throws Exception {

        String notValidName = "Pitesti";
        when(cityRepo.searchCityByName(notValidName)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/city/" + notValidName))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CityNotFoundByNameException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/city/{name} with valid credentials
            and valid name, we expect a city with the respective name.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getCityByValidNameTest() throws Exception {
        City city1 = new City();
        city1.setCityId(Long.valueOf(1));
        city1.setName("Bucuresti");
        city1.setZipcode("111111");
        String name = "Bucuresti";

        ObjectMapper objectMapper = new ObjectMapper();
        when(cityRepo.searchCityByName(name)).thenReturn(Optional.of(city1));

        mockMvc.perform(get("/api/v1/city/" + name))
                .andExpect(content().json(objectMapper.writeValueAsString(city1)));
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/city with valid credentials and authorities
            and a valid city that doesn't exist in the db, we expect 
            a 200 status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addCityWithoutAuthoritiesTest() throws Exception {
        City city1 = new City();
        city1.setCityId(Long.valueOf(1));
        city1.setName("Bucuresti");
        city1.setZipcode("111111");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(city1);

        mockMvc.perform(post("/api/v1/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/city with valid credentials and authorities
            and a city that already exists, we expect a CityAlreadyExistsException and a FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingCityTest() throws Exception {
        City city1 = new City();
        city1.setCityId(Long.valueOf(1));
        city1.setName("Bucuresti");
        city1.setZipcode("111111");

        when(cityRepo.searchCityByName(city1.getName())).thenReturn(Optional.of(city1));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(city1);

        mockMvc.perform(post("/api/v1/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CityAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/city with valid credentials and authorities
            and a valid city that doesn't exist in the db, we expect 
            a 200 status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidCityTest() throws Exception {
        City city1 = new City();
        city1.setCityId(Long.valueOf(1));
        city1.setName("Bucuresti");
        city1.setZipcode("111111");

        when(cityRepo.searchCityByName(city1.getName())).thenReturn(Optional.empty());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(city1);

        mockMvc.perform(post("/api/v1/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/city/{city_id} with valid credentials but
            not with appropriate authorities and a valid city id, we expect a 403 FORBIDDEN status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void deleteValidCityWithoutAuthoritiesTest() throws Exception {
        Long id = Long.valueOf(1);

        mockMvc.perform(delete("/api/v1/city/" + id))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/city/{city_id} with valid credentials and authorities
            and a valid city id, we expect a 200 status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void deleteValidCityTest() throws Exception {
        Long id = Long.valueOf(1);

        mockMvc.perform(delete("/api/v1/city/" + id))
                .andExpect(status().isOk());
    }


}
