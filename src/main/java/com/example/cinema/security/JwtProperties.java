package com.example.cinema.security;

// TODO: 6/27/2022 take properties from application.properties

public class JwtProperties {
    public static final String SECRET = "SomeSecret123";
    public static final int EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
