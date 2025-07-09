package com.homefixer.pagos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HomeFixer - Microservicio de Pagos")
                        .version("1.0")
                        .description("API REST para gestión de pagos en la plataforma HomeFixer")
                        .contact(new Contact()
                                .name("HomeFixer Team")
                                .email("dev@homefixer.com")));
    }
}
