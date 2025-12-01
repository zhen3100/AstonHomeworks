package com.evgeniy.spring.springapplication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management API")
                        .description("REST API for managing users with HATEOAS support")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com")));
    }
}