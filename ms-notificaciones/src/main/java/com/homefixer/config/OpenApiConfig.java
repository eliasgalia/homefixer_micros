package com.homefixer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Microservicio de Notificaciones")
                        .version("1.0")
                        .description("API RESTful para gestionar las notificaciones de usuarios y técnicos en la plataforma HomeFixer."));
    }
}