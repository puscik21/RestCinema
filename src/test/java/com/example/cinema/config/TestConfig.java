package com.example.cinema.config;

import com.example.cinema.MockService;
import com.example.cinema.repository.SpectatorRepository;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.UserPrincipalDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    public SpectatorRepository spectatorRepository;

    @Bean
    public UserPrincipalDetailsService userPrincipalDetailsService() {
        return new UserPrincipalDetailsService(spectatorRepository);
    }
}
