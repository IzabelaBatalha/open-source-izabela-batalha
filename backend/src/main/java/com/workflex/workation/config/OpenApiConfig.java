package com.workflex.workation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI workflexOpenApi(@Value("${server.port:8080}") int serverPort) {
        return new OpenAPI()
                .info(new Info()
                        .title("WorkFlex Workation API")
                        .description("REST API for listing workations imported from CSV.")
                        .version("1.0.0")
                        .contact(new Contact().name("WorkFlex Challenge")))
                .servers(List.of(new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Local development")));
    }
}
