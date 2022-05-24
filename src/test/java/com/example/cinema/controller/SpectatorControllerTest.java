package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.SpectatorDTO;
import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.SpectatorService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void shouldFindAll() throws Exception {
        List<Spectator> mockedSpectators = mockService.prepareSpectatorsList();
        when(spectatorService.findAll()).thenReturn(mockedSpectators);
        List<SpectatorDTO> expected = mapToDto(mockedSpectators);
        MvcResult result = mockMvc.perform(get(SPECTATORS_PATH))
                .andExpect(status().isOk())
                .andReturn();
        List<SpectatorDTO> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private List<SpectatorDTO> mapToDto(List<Spectator> spectators) {
        return spectators.stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @Test
    void shouldFindById() throws Exception {
        Spectator spectator = mockService.getSpectator();
        when(spectatorService.findByIdOrThrow(anyLong())).thenReturn(spectator);
        SpectatorDTO spectatorDTO = mappingService.map(spectator);
        String body = objectMapper.writeValueAsString(spectatorDTO);
        mockMvc.perform(get(SPECTATORS_PATH + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
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
        check400StatusForValidationException(spectatorDTO);
    }

    @Test
    void savingWithoutEmailShouldReturn400Status() throws Exception {
        SpectatorDTO spectatorDTO = mappingService.map(mockService.getSpectator());
        spectatorDTO.setEmail(null);
        check400StatusForValidationException(spectatorDTO);
    }

    @Test
    void savingWithoutPhoneNumberShouldReturn400Status() throws Exception {
        SpectatorDTO spectatorDTO = mappingService.map(mockService.getSpectator());
        spectatorDTO.setPhoneNumber(null);
        check400StatusForValidationException(spectatorDTO);
    }

    @Test
    void savingViolatedEmailShouldReturn400Status() throws Exception {
        SpectatorDTO spectatorDTO = mappingService.map(mockService.getSpectator());
        spectatorDTO.setEmail("somethingThatIsNotEmail");
        check400StatusForValidationException(spectatorDTO);
    }

    private void check400StatusForValidationException(SpectatorDTO spectatorDTO) throws Exception {
        String body = objectMapper.writeValueAsString(spectatorDTO);
        mockMvc.perform(post(SPECTATORS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}