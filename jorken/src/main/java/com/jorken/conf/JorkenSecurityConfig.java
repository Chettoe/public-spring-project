package com.jorken.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JorkenSecurityConfig {

    private final JorkenUserService userService;

    public JorkenSecurityConfig(JorkenUserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
        chainBuilder.authorizeHttpRequests(
                        configurer -> configurer
                                .requestMatchers("/", "/css/*").permitAll()
                                .requestMatchers("/jorken/admin/**").hasRole("ADMIN")
                                .requestMatchers("/jorken/betreuer/**").hasRole("BETREUER")
                                .anyRequest().authenticated()
                )
                .oauth2Login(conf -> conf.userInfoEndpoint(
                        info -> info.userService(userService)
                ));
        return chainBuilder.build();
    }
}
