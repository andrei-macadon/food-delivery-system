package com.example.fooddeliverysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.format.DateTimeFormatter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/spring-security-rest/api/v2/api-docs").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/v1/customer").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/v1/purchase").hasAnyAuthority("USER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/api/v1/**").authenticated()
                .mvcMatchers(HttpMethod.POST, "/api/v1/**").hasAuthority("ADMIN")
                .mvcMatchers(HttpMethod.DELETE, "/api/v1/**").hasAuthority("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/api/v1/**").hasAuthority("ADMIN")
                .and()
                .csrf().disable()
                .build();
    }
}
