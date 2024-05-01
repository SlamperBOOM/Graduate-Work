package com.slamperboom.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Backend address");

        Contact contact = new Contact();
        contact.setName("V. Shinkevich (SlamperBOOM)");
        contact.setEmail("slavashink@gmail.com");

        Info info = new Info();
        info.setTitle("Tax prediction API");
        info.setDescription("API definition for Tax prediction app");
        info.setContact(contact);
        info.setVersion("v1.0");
        
        return new OpenAPI().info(info).servers(List.of(server));
    }
}
