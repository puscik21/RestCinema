package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.ReservationDTO;
import com.example.cinema.entity.Reservation;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.ReservationService;
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
@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ReservationController controller;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockService mockService;

    @Autowired
    private RequestExceptionHandler requestExceptionHandler;

    private MockMvc mockMvc;

    private final String RESERVATIONS_PATH = "/reservations";

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(requestExceptionHandler)
                .build();
    }

    @Test
    void shouldFindAll() throws Exception {
        List<Reservation> mockedReservations = mockService.prepareReservationsList();
        when(reservationService.findAll()).thenReturn(mockedReservations);
        List<ReservationDTO> expected = mapToDto(mockedReservations);
        MvcResult result = mockMvc.perform(get(RESERVATIONS_PATH))
                .andExpect(status().isOk())
                .andReturn();
        List<ReservationDTO> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private List<ReservationDTO> mapToDto(List<Reservation> reservations) {
        return reservations.stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @Test
    void shouldFindById() throws Exception {
        Reservation reservation = mockService.getReservation();
        when(reservationService.getById(anyLong())).thenReturn(reservation);
        ReservationDTO reservationDTO = mappingService.map(reservation);
        String body = objectMapper.writeValueAsString(reservationDTO);
        mockMvc.perform(get(RESERVATIONS_PATH + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void reservationShouldBeAdded() throws Exception {
        Reservation reservation = mockService.getReservation();
        when(reservationService.save(any(Reservation.class))).thenReturn(reservation);
        ReservationDTO reservationDTO = mappingService.map(reservation);
        String body = objectMapper.writeValueAsString(reservationDTO);
        mockMvc.perform(post(RESERVATIONS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void savingWithoutSpectacleIdShouldReturn400Status() throws Exception {
        ReservationDTO reservationDTO = mappingService.map(mockService.getReservation());
        reservationDTO.setSpectacleId(null);
        check400StatusForValidationException(reservationDTO);
    }

    @Test
    void savingWithoutSeatIdShouldReturn400Status() throws Exception {
        ReservationDTO reservationDTO = mappingService.map(mockService.getReservation());
        reservationDTO.setSeatId(null);
        check400StatusForValidationException(reservationDTO);
    }

    @Test
    void savingWithoutSpectatorIdShouldReturn400Status() throws Exception {
        ReservationDTO reservationDTO = mappingService.map(mockService.getReservation());
        reservationDTO.setSpectatorId(null);
        check400StatusForValidationException(reservationDTO);
    }

    private void check400StatusForValidationException(ReservationDTO reservationDTO) throws Exception {
        String body = objectMapper.writeValueAsString(reservationDTO);
        mockMvc.perform(post(RESERVATIONS_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}