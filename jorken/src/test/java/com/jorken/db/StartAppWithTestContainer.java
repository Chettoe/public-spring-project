package com.jorken.db;

import com.jorken.JorkenApplication;
import org.springframework.boot.SpringApplication;

public class StartAppWithTestContainer {
    public static void main(String[] args) {
        SpringApplication.from(JorkenApplication::main)
                .with(ContainerConfig.class)
                .run(args);
    }
}
