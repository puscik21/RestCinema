package com.example.cinema.controller;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.AuditoriumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql(scripts = "classpath:test/testData.sql")
@SpringBootTest(classes = CinemaApplication.class)
public class AuditoriumControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestExceptionHandler exceptionHandler;

    @MockBean
    private AuditoriumService auditoriumService;

    @BeforeEach
    void setUp() {
        AuditoriumController mockedTestController = new AuditoriumController(auditoriumService);
        Mockito.when(auditoriumService.addAuditorium(Mockito.any(Auditorium.class))).thenReturn(new MockService().getAuditorium());
        mockMvc = MockMvcBuilders.standaloneSetup(mockedTestController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void auditoriumShouldBeAdded() throws Exception {
        Auditorium auditorium = new MockService().getAuditorium();
        String body = objectMapper.writeValueAsString(auditorium);
        mockMvc.perform(post("/auditorium")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(auditorium)));
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
