package com.lvlupgamer.productos.apiproductos.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz
                // Permitir TODOS los GET sin autenticación
                .requestMatchers("GET", "/api/usuarios/**").permitAll()
                // Permitir el registro sin autenticación
                .requestMatchers("POST", "/api/usuarios/registro").permitAll()
                // Requerir autenticación para otras operaciones
                .requestMatchers("POST", "/api/usuarios/**").authenticated()
                .requestMatchers("PUT", "/api/usuarios/**").authenticated()
                .requestMatchers("DELETE", "/api/usuarios/**").authenticated()
                // Permitir todo lo demás
                .anyRequest().permitAll()
            )
            .cors()
            .and()
            .httpBasic();
        
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}
