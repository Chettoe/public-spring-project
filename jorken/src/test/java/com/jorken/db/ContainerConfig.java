package com.jorken.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class ContainerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerConfig.class);

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        LOGGER.warn("Das hier ist ein Testcontainer");
        return new PostgreSQLContainer<>("postgres:18.1-alpine");
    }
}
