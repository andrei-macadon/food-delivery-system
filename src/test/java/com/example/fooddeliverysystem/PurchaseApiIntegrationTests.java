package com.example.fooddeliverysystem;

import com.example.fooddeliverysystem.dto.PurchaseDto;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.purchase.IncorrectDateFormatException;
import com.example.fooddeliverysystem.exceptions.purchase.PurchaseNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByIdException;
import com.example.fooddeliverysystem.model.*;
import com.example.fooddeliverysystem.repo.PurchaseRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


import com.fasterxml.jackson.datatype.jsr310.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseApiIntegrationTests {

    @MockBean
    private PurchaseRepo purchaseRepo;
    @MockBean
    private PasswordEncoder passwordEncoder;
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
    Logger logger = LoggerFactory.getLogger(PurchaseApiIntegrationTests.class);

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/purchase without being 
            authorized, then we expect a 401 Unauthorized response status. 
            """)
    public void checkAuthPurchaseTest() throws Exception {

        mockMvc.perform(get("/api/v1/purchase"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/purchase with valid credentials for
            authorization, then we expect a list with all the purchases from the db.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getAllPurchasesTest() throws Exception {

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

        DateTimeFormatter dateTimeFormatter = dateTimeFormatter();

        Purchase purchase1 = Purchase.builder()
                .purchaseId(Long.valueOf(1))
                .price(BigDecimal.valueOf(12))
                .restaurant(restaurant)
                .purchasePlacedTime(LocalDateTime.parse("2022-06-18 14:30", dateTimeFormatter))
                .menuItems(List.of(menuItem1))
                .build();

        Purchase purchase2 = Purchase.builder()
                .purchaseId(Long.valueOf(2))
                .price(BigDecimal.valueOf(14))
                .restaurant(restaurant)
                .purchasePlacedTime(LocalDateTime.parse("2022-07-09 16:45", dateTimeFormatter))
                .menuItems(List.of(menuItem1, menuItem2))
                .build();

        List<Purchase> purchases = List.of(purchase1, purchase2);

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        String responseJson = mapper.writeValueAsString(purchases);

        System.out.println("The response json is: " + responseJson);

        when(purchaseRepo.findAll()).thenReturn(purchases);

        mockMvc.perform(get("/api/v1/purchase"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/purchase/id/{id} with valid credentials and a non valid id,
            we expect to get a PurchaseNotFoundByIdException and a NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getPurchaseWithNonValidId() throws Exception {

        Long notValidId = Long.valueOf(1);

        when(purchaseRepo.findById(notValidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/purchase/id/" + notValidId))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PurchaseNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint GET /api/v1/purchase/id/{id} with valid credentials and a valid id,
            we expect to get the purchase with that id.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1")
    public void getPurchaseWithValidId() throws Exception {

        Long validId = Long.valueOf(1);

        ObjectMapper mapper = new ObjectMapper();
        when(purchaseRepo.findById(validId)).thenReturn(Optional.of(new Purchase()));

        mockMvc.perform(get("/api/v1/purchase/id/" + validId))
                .andExpect(content().json(mapper.writeValueAsString(new Purchase())));
    }


    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/purchase with valid credentials, valid authorities,
            and a purchase dto that has an incorrect date format, we expect to get
            an IncorrectDateFormatException
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addPurchaseWithIncorrectDateFormat() throws Exception {

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

        PurchaseDto purchaseDto = PurchaseDto.builder()
                .restaurant(restaurant)
                .menuItems(List.of(menuItem1, menuItem2))
                .customer(customer1)
                .purchasePlacedTime("16:45:30")
                .build();

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        String requestJson = mapper.writeValueAsString(purchaseDto);


//        mockMvc.perform(post("/api/v1/purchase")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IncorrectDateFormatException));
    }

    @Test
    @DisplayName("""
            When we call the endpoint POST /api/v1/purchase with valid credentials, valid authorities,
            and a valid purchase dto, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "USER")
    public void addPurchase() throws Exception {

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

        PurchaseDto purchaseDto = PurchaseDto.builder()
                .restaurant(restaurant)
                .menuItems(List.of(menuItem1, menuItem2))
                .customer(customer1)
                .purchasePlacedTime("2022-06-08 16:45")
                .build();

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        String requestJson = mapper.writeValueAsString(purchaseDto);

        mockMvc.perform(post("/api/v1/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            When we call the endpoint PUT /api/v1/purchase/{id} with valid credentials, valid authorities,
            and a non valid purchase id, we expect to get a PurchaseNotFoundByIdException and a 
            NOT_FOUND status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void updatePurchaseWithNonValidId() throws Exception {

        Long nonValidId = Long.valueOf(1);
        String actualDeliveryTime = "2022-06-08 16:45";

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        String requestJson = mapper.writeValueAsString(actualDeliveryTime);

        when(purchaseRepo.findById(nonValidId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/purchase/" + nonValidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PurchaseNotFoundByIdException))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("""
            When we call the endpoint PUT /api/v1/purchase/{id} with valid credentials, valid authorities,
            and a valid purchase id, we expect to get a 200 OK status code.
            """)
    @WithMockUser(username = "andrei", password = "andreiparola1", authorities = "ADMIN")
    public void updatePurchase() throws Exception {

        Long validId = Long.valueOf(1);
        String actualDeliveryTime  = "2022-06-08 16:45";

        when(purchaseRepo.findById(validId)).thenReturn(Optional.of(new Purchase()));

        mockMvc.perform(put("/api/v1/purchase/" + validId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actualDeliveryTime))
                .andExpect(status().isOk());
    }
}
