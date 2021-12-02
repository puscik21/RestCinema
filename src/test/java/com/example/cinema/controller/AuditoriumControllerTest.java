package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.AuditoriumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = CinemaApplication.class)
public class AuditoriumControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestExceptionHandler exceptionHandler;

    @Mock
    private AuditoriumService auditoriumService;

    @BeforeEach
    void setUp() {
        AuditoriumController mockedTestController = new AuditoriumController(auditoriumService);
        mockMvc = MockMvcBuilders.standaloneSetup(mockedTestController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void return400ForViolatedAuditorium() throws Exception {
        Auditorium auditorium = new Auditorium(0, 5);
        String body = objectMapper.writeValueAsString(auditorium);

        mockMvc.perform(post("/auditorium")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}
