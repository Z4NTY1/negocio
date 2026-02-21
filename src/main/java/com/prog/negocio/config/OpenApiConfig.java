package com.prog.negocio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger) para documentación de la API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Negocio Empanadas")
                        .description("Sistema de gestión de ventas, gastos, productos y cierres quincenales.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Negocio")));
    }
}
