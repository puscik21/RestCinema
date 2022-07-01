package com.example.cinema.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Getter
@Setter
@Configuration("jwtProperties")
@ConfigurationProperties(prefix = "jwt")
@PropertySource(name = "jwtProperties", value = "jwtProperties.properties")
public class JwtProperties {
    private String secret;
    private String tokenPrefix;
    private String headerKey;
    private int expirationTime;
}
