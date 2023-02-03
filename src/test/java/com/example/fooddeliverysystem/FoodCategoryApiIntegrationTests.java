package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByNameException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByIdException;
import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.model.FoodCategory;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.repo.FoodCategoryRepo;
import com.example.fooddeliverysystem.repo.RestaurantRepo;
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
public class FoodCategoryApiIntegrationTests {

    @MockBean
    private FoodCategoryRepo foodCategoryRepo;
    @MockBean
    private RestaurantRepo restaurantRepo;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/foodcategory without being 
            authorized, then we expect a 401 Unauthorized response status. 
            """)
    public void checkAuthFoodCategoryTest() throws Exception {

        mockMvc.perform(get("/api/v1/foodcategory"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/foodcategory with valid credentials for
            authorization, then we expect a list with all the food categories from the db.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getAllFoodCategoriesTest() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        FoodCategory foodCategory2 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(2))
                .foodCategoryName("foodcateg2")
                .restaurantMenu(restaurant)
                .build();

        FoodCategory foodCategory3 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(3))
                .foodCategoryName("foodcateg3")
                .restaurantMenu(restaurant)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        List<FoodCategory> foodCategoryList = List.of(foodCategory1, foodCategory2, foodCategory3);

        when(foodCategoryRepo.findAll()).thenReturn(foodCategoryList);

        mockMvc.perform(get("/api/v1/foodcategory")).andExpect(
                content().json(mapper.writeValueAsString(foodCategoryList))
        );
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/foodcategory/id/{id} with valid credentials and a non valid id,
            we expect to get a FoodCategoryNotFoundByIdException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getFoodCategoryWithNonValidId() throws Exception {

        Long notValidId = Long.valueOf(1);

        when(foodCategoryRepo.findById(notValidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/foodcategory/id/" + notValidId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodCategoryNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/foodcategory/id/{id} with valid credentials and a valid id,
            we expect to get the food category with that id.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void getFoodCategoryWithValidId() throws Exception {

        Long validId = Long.valueOf(1);

        ObjectMapper mapper = new ObjectMapper();
        when(foodCategoryRepo.findById(validId)).thenReturn(Optional.of(new FoodCategory()));

        mockMvc.perform(get("/api/v1/foodcategory/id/" + validId))
                .andExpect(content().json(mapper.writeValueAsString(new FoodCategory())));
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/foodcategory/{name} with valid credentials and a non valid name,
            we expect to get a FoodCategoryNotFoundByNameException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getFoodCategoryWithNonValidName() throws Exception {

        String notValidName = "rest1";

        when(foodCategoryRepo.searchFoodCategoryByName(notValidName)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/foodcategory/" + notValidName))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodCategoryNotFoundByNameException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/foodcategory/{name} with valid credentials and a valid name,
            we expect to get the restaurant with that name.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getFoodCategoryWithValidName() throws Exception {

        String foodCateogoryName = "Rest1";

        ObjectMapper mapper = new ObjectMapper();
        when(foodCategoryRepo.searchFoodCategoryByName(foodCateogoryName)).thenReturn(Optional.of(new FoodCategory()));

        mockMvc.perform(get("/api/v1/foodcategory/" + foodCateogoryName))
                .andExpect(content().json(mapper.writeValueAsString(new FoodCategory())));
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/foodcategory with valid credentials but without the
            appropriate authorities, we expect to get a 403 FORBIDDEN status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addFoodCategoryWithoutAuthorities() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory1);

        mockMvc.perform(post("/api/v1/foodcategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/foodcategory with valid credentials, valid authorities,
            and a foodcategory that already exists, we expect to get a FoodCategoryAlreadyExistsException and a
            FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingFoodCategory() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory1);

        when(foodCategoryRepo.searchFoodCategoryByName(foodCategory1.getFoodCategoryName())).thenReturn(Optional.of(new FoodCategory()));


        mockMvc.perform(post("/api/v1/foodcategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodCategoryAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/foodcategory with valid credentials, valid authorities,
            and a valid food category, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidFoodCategory() throws Exception {

        City city = City.builder()
                .cityId(Long.valueOf(1))
                .name("Bucuresti")
                .zipcode("111111")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .restaurantId(Long.valueOf(1))
                .name("Rest1")
                .city(city)
                .address("Adresa 1")
                .build();

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory1);

        when(foodCategoryRepo.searchFoodCategoryByName(foodCategory1.getFoodCategoryName()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/foodcategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/foodcategory/{restaurant_id} with valid credentials, valid authorities,
            and an already existing foodCategory, we expect to get a FoodCategoryAlreadyExistsException and 
            a FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingFoodCategoryToRestaurant() throws Exception {

        Long restaurantId = Long.valueOf(1);

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory1);

        when(foodCategoryRepo.searchFoodCategoryByName(foodCategory1.getFoodCategoryName()))
                .thenReturn(Optional.of(foodCategory1));

        mockMvc.perform(post("/api/v1/foodcategory/" + restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodCategoryAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/foodcategory/{restaurant_id} with valid credentials, valid authorities,
            and an already existing foodCategory, we expect to get a FoodCategoryAlreadyExistsException and 
            a FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidFoodCategoryToNonValidRestaurant() throws Exception {

        Long nonValidRestaurantId = Long.valueOf(1);

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory1);

        when(foodCategoryRepo.searchFoodCategoryByName(foodCategory1.getFoodCategoryName()))
                .thenReturn(Optional.empty());


        when(restaurantRepo.findById(nonValidRestaurantId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/foodcategory/" + nonValidRestaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RestaurantNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/foodcategory/{restaurant_id} with valid credentials, valid authorities,
            and a valid foodCategory, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidFoodCategoryToRestaurant() throws Exception {

        Long restaurantId = Long.valueOf(1);

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory1);

        when(foodCategoryRepo.searchFoodCategoryByName(foodCategory1.getFoodCategoryName()))
                .thenReturn(Optional.empty());

        when(restaurantRepo.findById(restaurantId)).thenReturn(Optional.of(new Restaurant()));

        mockMvc.perform(post("/api/v1/foodcategory/" + restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/foodcategory/{id} with valid credentials, valid authorities,
            and a foodcategory id, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void deleteFoodCategoryById() throws Exception {

        Long foodCategoryId = Long.valueOf(1);

        mockMvc.perform(delete("/api/v1/foodcategory/" + foodCategoryId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/foodcategory/{id} with valid credentials, valid authorities,
            and a non valid foodcategory id, we expect to get a FoodCategoryNotFoundByIdException and a 
            NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void updateFoodCategoryWithNonValidId() throws Exception {

        Long foodCategoryId = Long.valueOf(1);

        FoodCategory foodCategory1 = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory1);

        when(foodCategoryRepo.searchFoodCategoryByName(foodCategory1.getFoodCategoryName()))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/foodcategory/" + foodCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodCategoryNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/restaurant/restaurant_id with valid credentials, valid authorities,
            and a restaurant id, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void updateFoodCategoryWithValidRequestBody() throws Exception {

        Long foodCategoryId = Long.valueOf(1);

        FoodCategory foodCategory = FoodCategory.builder()
                .foodCategoryName("Something")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(foodCategory);

        when(foodCategoryRepo.findById(foodCategoryId))
                .thenReturn(Optional.of(new FoodCategory()));

        mockMvc.perform(put("/api/v1/foodcategory/" + foodCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }
}
