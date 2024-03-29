package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.SeatDTO;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.SeatService;
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
@WebMvcTest(controllers = SeatController.class)
class SeatControllerTest {

    @MockBean
    private SeatService seatService;

    @Autowired
    private SeatController controller;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockService mockService;

    @Autowired
    private RequestExceptionHandler requestExceptionHandler;

    private MockMvc mockMvc;

    private final String SEATS_PATH = "/seats";

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(requestExceptionHandler)
                .build();
    }

    @Test
    void shouldFindAll() throws Exception {
        List<Seat> mockedSeats = mockService.prepareSeatsList();
        when(seatService.findAll()).thenReturn(mockedSeats);
        List<SeatDTO> expected = mapToDto(mockedSeats);
        MvcResult result = mockMvc.perform(get(SEATS_PATH))
                .andExpect(status().isOk())
                .andReturn();
        List<SeatDTO> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private List<SeatDTO> mapToDto(List<Seat> seats) {
        return seats.stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @Test
    void shouldFindById() throws Exception {
        Seat seat = mockService.getSeat();
        when(seatService.getById(anyLong())).thenReturn(seat);
        SeatDTO seatDTO = mappingService.map(seat);
        String body = objectMapper.writeValueAsString(seatDTO);
        mockMvc.perform(get(SEATS_PATH + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void seatShouldBeAdded() throws Exception {
        Seat seat = mockService.getSeat();
        when(seatService.save(any(Seat.class))).thenReturn(seat);
        SeatDTO seatDTO = mappingService.map(seat);
        String body = objectMapper.writeValueAsString(seatDTO);
        mockMvc.perform(post(SEATS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void savingWithoutAuditoriumIdShouldReturn400Status() throws Exception {
        SeatDTO seatDTO = mappingService.map(mockService.getSeat());
        seatDTO.setAuditoriumId(null);
        check400StatusForValidationException(seatDTO);
    }

    @Test
    void savingWithoutNumberShouldReturn400Status() throws Exception {
        SeatDTO seatDTO = mappingService.map(mockService.getSeat());
        seatDTO.setNumber(null);
        check400StatusForValidationException(seatDTO);
    }

    @Test
    void savingWithoutIsReservedShouldReturn400Status() throws Exception {
        SeatDTO seatDTO = mappingService.map(mockService.getSeat());
        seatDTO.setIsReserved(null);
        check400StatusForValidationException(seatDTO);
    }

    @Test
    void savingViolatedNumberShouldReturn400Status() throws Exception {
        SeatDTO seatDTO = mappingService.map(mockService.getSeat());
        seatDTO.setNumber(0);
        check400StatusForValidationException(seatDTO);
    }

    private void check400StatusForValidationException(SeatDTO seatDTO) throws Exception {
        String body = objectMapper.writeValueAsString(seatDTO);
        mockMvc.perform(post(SEATS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}