package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.SpectatorDTO;
import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.SpectatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = SpectatorController.class)
class SpectatorControllerTest {

    @MockBean
    private SpectatorService spectatorService;

    @Autowired
    private SpectatorController controller;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockService mockService;

    @Autowired
    private RequestExceptionHandler requestExceptionHandler;

    private MockMvc mockMvc;

    private final String SPECTATORS_PATH = "/spectators";

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(requestExceptionHandler)
                .build();
    }

    @Test
    void spectatorShouldBeAdded() throws Exception {
        Spectator spectator = mockService.getSpectator();
        when(spectatorService.save(any(Spectator.class))).thenReturn(spectator);
        SpectatorDTO spectatorDTO = mappingService.map(spectator);
        String body = objectMapper.writeValueAsString(spectatorDTO);
        mockMvc.perform(post(SPECTATORS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void savingWithoutNameShouldReturn400Status() throws Exception {
        SpectatorDTO spectatorDTO = mappingService.map(mockService.getSpectator());
        spectatorDTO.setName(null);
        String body = objectMapper.writeValueAsString(spectatorDTO);
        mockMvc.perform(post(SPECTATORS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }

    @Test
    void savingWithoutEmailShouldReturn400Status() throws Exception {
        SpectatorDTO spectatorDTO = mappingService.map(mockService.getSpectator());
        spectatorDTO.setEmail(null);
        String body = objectMapper.writeValueAsString(spectatorDTO);
        mockMvc.perform(post(SPECTATORS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }

    @Test
    void savingWithoutPhoneNumberShouldReturn400Status() throws Exception {
        SpectatorDTO spectatorDTO = mappingService.map(mockService.getSpectator());
        spectatorDTO.setPhoneNumber(null);
        String body = objectMapper.writeValueAsString(spectatorDTO);
        mockMvc.perform(post(SPECTATORS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }

    @Test
    void savingViolatedEmailShouldReturn400Status() throws Exception {
        SpectatorDTO spectatorDTO = mappingService.map(mockService.getSpectator());
        spectatorDTO.setEmail("somethingThatIsNotEmail");
        String body = objectMapper.writeValueAsString(spectatorDTO);
        mockMvc.perform(post(SPECTATORS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}