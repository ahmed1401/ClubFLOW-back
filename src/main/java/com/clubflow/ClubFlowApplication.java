package com.clubflow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@OpenAPIDefinition(
        info = @Info(
                title = "Club Flow API",
                version = "1.0.0",
                description = "API for Club Flow Management System",
                contact = @Contact(
                        name = "Support Team",
                        email = "ahmed.hajjej@enicar.ucar.tn.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                )
        }
)
public class ClubFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubFlowApplication.class, args);
    }
}