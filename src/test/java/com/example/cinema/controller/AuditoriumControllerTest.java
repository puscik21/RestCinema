package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.AuditoriumDTO;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.AuditoriumService;
import com.example.cinema.service.MappingService;
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
@WebMvcTest(controllers = AuditoriumController.class)
public class AuditoriumControllerTest {

    @MockBean
    private AuditoriumService auditoriumService;

    @Autowired
    private AuditoriumController controller;

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
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(requestExceptionHandler)
                .build();
    }

    @Test
    void shouldFindAll() throws Exception {
        List<Auditorium> mockedAuditoriums = mockService.prepareAuditoriumsList();
        when(auditoriumService.findAll()).thenReturn(mockedAuditoriums);
        List<AuditoriumDTO> expected = mapToDto(mockedAuditoriums);
        MvcResult result = mockMvc.perform(get(AUDITORIUMS_PATH))
                .andExpect(status().isOk())
                .andReturn();
        List<AuditoriumDTO> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private List<AuditoriumDTO> mapToDto(List<Auditorium> auditoriums) {
        return auditoriums.stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @Test
    void shouldFindById() throws Exception {
        Auditorium auditorium = mockService.getAuditorium();
        when(auditoriumService.getById(anyLong())).thenReturn(auditorium);
        AuditoriumDTO auditoriumDTO = mappingService.map(auditorium);
        String body = objectMapper.writeValueAsString(auditoriumDTO);
        mockMvc.perform(get(AUDITORIUMS_PATH + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void auditoriumShouldBeAdded() throws Exception {
        Auditorium auditorium = mockService.getAuditorium();
        when(auditoriumService.save(any(Auditorium.class))).thenReturn(auditorium);
        AuditoriumDTO auditoriumDTO = mappingService.map(auditorium);
        String body = objectMapper.writeValueAsString(auditoriumDTO);
        mockMvc.perform(post(AUDITORIUMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void savingWithoutNumberShouldReturn400Status() throws Exception {
        AuditoriumDTO auditoriumDTO = mappingService.map(mockService.getAuditorium());
        auditoriumDTO.setNumber(null);
        check400StatusForValidationException(auditoriumDTO);
    }

    @Test
    void savingViolatedNumberShouldReturn400Status() throws Exception {
        AuditoriumDTO auditoriumDTO = mappingService.map(new Auditorium(0, 5));
        check400StatusForValidationException(auditoriumDTO);
    }

    private void check400StatusForValidationException(AuditoriumDTO auditoriumDTO) throws Exception {
        String body = objectMapper.writeValueAsString(auditoriumDTO);
        mockMvc.perform(post(AUDITORIUMS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}
