package com.lvlupgamer.productos.apiproductos.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "ApiKeyAuth";
        
        return new OpenAPI()
            .info(new Info()
                .title("API Productos - Level Up Gamer")
                .version("v1.0")
                .description("API REST para gestión de productos y categorías")
                .contact(new Contact()
                    .name("Level Up Gamer")
                    .email("soporte@levelupgamer.com")))
            // ⭐ Agregar seguridad (opcional)
            .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name("x-api-key")
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .description("Ingresa tu API Key")));
    }
}
