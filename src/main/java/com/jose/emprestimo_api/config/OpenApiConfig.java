package com.jose.emprestimo_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI emprestimoApiOpenAPI() {

                return new OpenAPI()
                                .info(new Info()
                                                .title("API de Empréstimos")
                                                .version("1.0.0")
                                                .description(
                                                                "API REST para gerenciamento de clientes e empréstimos. "
                                                                                + "Possui operações CRUD, validações, análise de empréstimos, "
                                                                                + "filtros por status e persistência em MySQL."));
        }
}