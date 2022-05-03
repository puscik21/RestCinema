package com.example.cinema.config;

import com.example.cinema.service.MappingService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public MappingService mappingService() {
        return new MappingService(modelMapper());
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MockService mockService() {
        return new MockService();
    }
}
