package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByNameException;
import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.model.FoodCategory;
import com.example.fooddeliverysystem.model.MenuItem;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.repo.FoodCategoryRepo;
import com.example.fooddeliverysystem.repo.MenuItemRepo;
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
public class MenuItemApiIntegrationTests {

    @MockBean
    private MenuItemRepo menuItemRepo;
    @MockBean
    private FoodCategoryRepo foodCategoryRepo;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/menuitem without being 
            authorized, then we expect a 401 Unauthorized response status. 
            """)
    public void checkAuthRestaurantTest() throws Exception {

        mockMvc.perform(get("/api/v1/menuitem"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/menuitem with valid credentials for
            authorization, then we expect a list with all the menu items from the db.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getAllRestaurantsTest() throws Exception {

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

        FoodCategory foodCategory = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .foodCategory(foodCategory)
                .build();

        MenuItem menuItem2 = MenuItem.builder()
                .ingredients("There’s a reason this is one of the most popular types of pizza. Who doesn’t love biting" +
                        " into a crispy, salty round of pepperoni?")
                .name("Pepperoni Pizza")
                .price(BigDecimal.valueOf(32))
                .timeToCook(BigDecimal.valueOf(17))
                .foodCategory(foodCategory)
                .build();

        List<MenuItem> menuItemList = List.of(menuItem1, menuItem2);

        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(menuItemList);

        when(menuItemRepo.findAll()).thenReturn(menuItemList);

        mockMvc.perform(get("/api/v1/menuitem")).andExpect(
                content().json(responseJson)
        );
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/menuitem/id/{id} with valid credentials and a non valid id,
            we expect to get a MenuItemNotFoundByIdException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getMenuItemByNonValidId() throws Exception {

        Long notValidId = Long.valueOf(1);

        when(menuItemRepo.findById(notValidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/menuitem/id/" + notValidId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MenuItemNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/menuitem/id/{id} with valid credentials and a valid id,
            we expect to get the menu item with that id.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getMenuItemById() throws Exception {

        Long validId = Long.valueOf(1);

        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(new MenuItem());

        when(menuItemRepo.findById(validId)).thenReturn(Optional.of(new MenuItem()));

        mockMvc.perform(get("/api/v1/menuitem/id/" + validId))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/menuitem/{foodcategory_id} with valid credentials and a non valid 
            food category id, we expect to get a FoodCategoryNotFoundByIdException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getMenuItemsByNonValidFoodCategory() throws Exception {

        Long nonValidFoodCategoryId = Long.valueOf(1);

        when(foodCategoryRepo.findById(nonValidFoodCategoryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/menuitem/" + nonValidFoodCategoryId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodCategoryNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/menuitem/{foodcategory_id} with valid credentials and a valid 
            food category id, we expect to get the list of menu items with that specific food category id.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getMenuItemsByFoodCategory() throws Exception {

        Long validFoodCategoryId = Long.valueOf(1);

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

        FoodCategory foodCategory = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .foodCategory(foodCategory)
                .build();

        MenuItem menuItem2 = MenuItem.builder()
                .ingredients("There’s a reason this is one of the most popular types of pizza. Who doesn’t love biting" +
                        " into a crispy, salty round of pepperoni?")
                .name("Pepperoni Pizza")
                .price(BigDecimal.valueOf(32))
                .timeToCook(BigDecimal.valueOf(17))
                .foodCategory(foodCategory)
                .build();

        List<MenuItem> menuItemList = List.of(menuItem1, menuItem2);
        ObjectMapper mapper = new ObjectMapper();
        String responseJson = mapper.writeValueAsString(menuItemList);

        when(foodCategoryRepo.findById(validFoodCategoryId)).thenReturn(Optional.of(new FoodCategory()));

        when(menuItemRepo.searchMenuItemsByFoodCategory(validFoodCategoryId))
                .thenReturn(menuItemList);

        mockMvc.perform(get("/api/v1/menuitem/" + validFoodCategoryId))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/menuitem with valid credentials but without the
            appropriate authorities, we expect to get a 403 FORBIDDEN status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addMenuItemWithoutAuthorities() throws Exception {

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

        FoodCategory foodCategory = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .foodCategory(foodCategory)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        mockMvc.perform(post("/api/v1/menuitem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/menuitem with valid credentials, valid authorities,
            and a menu item that already exists, we expect to get a MenuItemAlreadyExistsException and a
            FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingMenuItem() throws Exception {

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

        FoodCategory foodCategory = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .foodCategory(foodCategory)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        when(menuItemRepo.searchMenuItemByName(menuItem1.getName())).thenReturn(Optional.of(new MenuItem()));

        mockMvc.perform(post("/api/v1/menuitem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MenuItemAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/menuitem with valid credentials, valid authorities,
            and a valid menu item, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidMenuItem() throws Exception {

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

        FoodCategory foodCategory = FoodCategory.builder()
                .foodCategoryId(Long.valueOf(1))
                .foodCategoryName("foodcateg1")
                .restaurantMenu(restaurant)
                .build();

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .foodCategory(foodCategory)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        when(menuItemRepo.searchMenuItemByName(menuItem1.getName())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/menuitem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/menuitem with valid credentials, valid authorities,
            and a non valid food category, we expect to get a FoodCategoryNotFoundByIdException and a
            NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addMenuItemToNonValidFoodCategory() throws Exception {

        Long nonValidFoodCategoryId = Long.valueOf(1);

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        when(menuItemRepo.searchMenuItemByName(menuItem1.getName())).thenReturn(Optional.empty());

        when(foodCategoryRepo.findById(nonValidFoodCategoryId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/menuitem/" + nonValidFoodCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof FoodCategoryNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/menuitem with valid credentials, valid authorities,
            and an already existing menu item, we expect to get a MenuItemAlreadyExistsException and a
            FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addAlreadyExistingMenuItemToFoodCategory() throws Exception {

        Long validFoodCategoryId = Long.valueOf(1);

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        when(menuItemRepo.searchMenuItemByName(menuItem1.getName())).thenReturn(Optional.of(new MenuItem()));

        when(foodCategoryRepo.findById(validFoodCategoryId)).thenReturn(Optional.of(new FoodCategory()));

        mockMvc.perform(post("/api/v1/menuitem/" + validFoodCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MenuItemAlreadyExistsException))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/menuitem with valid credentials, valid authorities,
            and a valid food category id and a valid menu item, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void addValidMenuItemToFoodCategory() throws Exception {

        Long validFoodCategoryId = Long.valueOf(1);

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        when(menuItemRepo.searchMenuItemByName(menuItem1.getName())).thenReturn(Optional.empty());

        when(foodCategoryRepo.findById(validFoodCategoryId)).thenReturn(Optional.of(new FoodCategory()));

        mockMvc.perform(post("/api/v1/menuitem/" + validFoodCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint DELETE /api/v1/menuitem/{id} with valid credentials, valid authorities,
            and a menuitem id, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void deleteMenuItemById() throws Exception {

        Long menuItemId = Long.valueOf(1);

        mockMvc.perform(delete("/api/v1/menuitem/" + menuItemId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint PUT /api/v1/menuitem/{id} with valid credentials, valid authorities,
            and a non valid menuitem id, we expect to get a MenuItemNotFoundByIdException and a 
            NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void updateMenuItemWithNonValidId() throws Exception {

        Long menuItemId = Long.valueOf(1);

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        when(menuItemRepo.findById(menuItemId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/menuitem/" + menuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MenuItemNotFoundByIdException))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("""
            When we call the endpoint PUT /api/v1/menuitem/{id} with valid credentials, valid authorities,
            and a valid menuitem id, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void updateFoodCategoryWithValidRequestBody() throws Exception {

        Long menuItemId = Long.valueOf(1);

        MenuItem menuItem1 = MenuItem.builder()
                .ingredients("If pepperoni just isn’t enough, and you’re looking for a pie with a bit more heft, a " +
                        "meat pizza is a perfect and popular choice. Pile on ground beef and sausage for a hearty meal.")
                .name("Meat Pizza")
                .price(BigDecimal.valueOf(35))
                .timeToCook(BigDecimal.valueOf(20))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(menuItem1);

        when(menuItemRepo.findById(menuItemId))
                .thenReturn(Optional.of(new MenuItem()));

        mockMvc.perform(put("/api/v1/menuitem/" + menuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }
}
