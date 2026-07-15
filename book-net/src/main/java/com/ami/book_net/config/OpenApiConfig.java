package com.ami.book_net.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        // general metadata about the API
        info = @Info(
                contact = @Contact(
                        name = "Amandi",
                        email = "amandinimasha99@gmail.com",
                        url = "https://www.nimasha.me"
                ),
                description = "OpenApi documentation for Spring security",
                title = "OpenApi specification - Amandi",
                version = "1.0.0",
                license = @License(
                        name = "Licence name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        // Environment Switchers: This allows you to change the base URL of your API requests directly within the Swagger UI web page.
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/api/v1"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://book-net-api.herokuapp.com/api/v1"
                )
        },
        security = {
                //  JWT Security Configuration
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
// JWT Security Configuration
@SecurityScheme(
        name ="bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
