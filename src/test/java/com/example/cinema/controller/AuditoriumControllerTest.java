package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.AuditoriumDTO;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.AuditoriumService;
import com.example.cinema.service.MappingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = AuditoriumController.class)
public class AuditoriumControllerTest {

    @MockBean
    private AuditoriumService auditoriumService;

    @Autowired
    private AuditoriumController auditoriumController;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockService mockService;

    @Autowired
    private RequestExceptionHandler requestExceptionHandler;

    private MockMvc mockMvc;

    private final String AUDITORIUMS_PATH = "/auditoriums";

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditoriumController)
                .setControllerAdvice(requestExceptionHandler)
                .build();
    }

    @Test
    void auditoriumShouldBeAdded() throws Exception {
        Mockito.when(auditoriumService.addAuditorium(Mockito.any(Auditorium.class))).thenReturn(mockService.getAuditorium());
        AuditoriumDTO auditoriumDTO = mappingService.map(mockService.getAuditorium());
        String body = objectMapper.writeValueAsString(auditoriumDTO);
        mockMvc.perform(post(AUDITORIUMS_PATH)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void return400ForViolatedAuditorium() throws Exception {
        AuditoriumDTO auditoriumDTO = mappingService.map(new Auditorium(0, 5));
        String body = objectMapper.writeValueAsString(auditoriumDTO);
        mockMvc.perform(post(AUDITORIUMS_PATH)
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}
