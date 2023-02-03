package com.example.fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "The customer name can not be blank.")
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "city_id",
            referencedColumnName = "cityId"
    )
    @NotNull(message = "The customer must be part of a country.")
    private City city;

    @NotBlank(message = "The customer must have an address.")
    @Size(min = 5, message = "The address must be at least 5 characters long.")
    private String address;

    @NotBlank(message = "The customer's phone number can not be blank.")
    @Size(min = 10, max = 10, message = "The phone number must be exact 10 characters long.")
    private String phone;

    @NotBlank(message = "The customer's email can not be null")
    @Pattern(regexp = "[a-z0-9]+@[a-z]+\\.[a-z]{2,3}", message = "The email must be valid.")
    private String email;
    @NotBlank(message = "The password can not be blank.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "The password must have at least eight characters, at least one letter and one number")
    private String password;
//    mappedBy = "customers",
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customer_roles",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @NotNull(message = "The user must be assigned a role.")
    private List<Role> roles;

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.REMOVE
    )
    @JsonIgnore
    private List<Purchase> purchases;


    public Customer() {
    }

    public Customer(Long customerId, String name, City city, String address, String phone, String email,
                    String password, List<Role> roles, List<Purchase> purchases) {
        this.customerId = customerId;
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.purchases = purchases;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }
}
