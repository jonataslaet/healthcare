package com.jonataslaet.healthcare.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI healthcareOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Healthcare API")
                .description("API REST para gerenciamento de pacientes")
                .version("v0.0.1")
                .contact(new Contact()
                    .name("Jonatas Laet")
                    .email("jonataslaetprogramador@gmail.com")));
    }
}
