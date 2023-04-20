package com.availability.ja;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.jruby.util.log.Logger;
import org.jruby.util.log.LoggerFactory;
import org.springframework.boot.SpringApplication;


@org.springframework.boot.autoconfigure.SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})

public class SpringBootApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }

}
