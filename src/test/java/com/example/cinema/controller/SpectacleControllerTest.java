package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.SpectacleDTO;
import com.example.cinema.entity.Spectacle;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.SpectacleService;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = SpectacleController.class)
class SpectacleControllerTest {

    @MockBean
    private SpectacleService spectacleService;

    @Autowired
    private SpectacleController controller;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockService mockService;

    @Autowired
    private RequestExceptionHandler requestExceptionHandler;

    private MockMvc mockMvc;

    private final String SPECTACLES_PATH = "/spectacles";

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(requestExceptionHandler)
                .build();
    }

    @Test
    void shouldFindAll() throws Exception {
        List<Spectacle> mockedSpectacles = mockService.prepareSpectaclesList();
        when(spectacleService.findAll()).thenReturn(mockedSpectacles);
        List<SpectacleDTO> expected = mapToDto(mockedSpectacles);
        MvcResult result = mockMvc.perform(get(SPECTACLES_PATH))
                .andExpect(status().isOk())
                .andReturn();
        List<SpectacleDTO> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private List<SpectacleDTO> mapToDto(List<Spectacle> spectacles) {
        return spectacles.stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @Test
    void spectacleShouldBeAdded() throws Exception {
        Spectacle spectacle = mockService.getSpectacle();
        when(spectacleService.save(any(Spectacle.class))).thenReturn(spectacle);
        SpectacleDTO spectacleDTO = mappingService.map(spectacle);
        String body = objectMapper.writeValueAsString(spectacleDTO);
        mockMvc.perform(post(SPECTACLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void savingWithoutAuditoriumIdShouldReturn400Status() throws Exception {
        SpectacleDTO spectacleDTO = mappingService.map(mockService.getSpectacle());
        spectacleDTO.setAuditoriumId(null);
        check400StatusForValidationException(spectacleDTO);
    }

    @Test
    void savingWithoutMovieIdShouldReturn400Status() throws Exception {
        SpectacleDTO spectacleDTO = mappingService.map(mockService.getSpectacle());
        spectacleDTO.setMovieId(null);
        check400StatusForValidationException(spectacleDTO);
    }

    @Test
    void savingWithoutDateTimeShouldReturn400Status() throws Exception {
        SpectacleDTO spectacleDTO = mappingService.map(mockService.getSpectacle());
        spectacleDTO.setDateTime(null);
        check400StatusForValidationException(spectacleDTO);
    }

    private void check400StatusForValidationException(SpectacleDTO spectacleDTO) throws Exception {
        String body = objectMapper.writeValueAsString(spectacleDTO);
        mockMvc.perform(post(SPECTACLES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}