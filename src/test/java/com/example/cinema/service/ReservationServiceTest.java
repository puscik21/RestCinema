package com.example.cinema.service;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Reservation;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CinemaApplication.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private SpectacleService spectacleService;
    @Mock
    private SpectatorService spectatorService;
    @Mock
    private SeatService seatService;


    @Autowired
    private MockService mockService;

    private ReservationService reservationService;


    @BeforeEach
    void setUp() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        spectacleService = Mockito.mock(SpectacleService.class);
        spectatorService = Mockito.mock(SpectatorService.class);
        seatService = Mockito.mock(SeatService.class);
        reservationService = new ReservationService(reservationRepository, spectacleService, spectatorService, seatService);
        Mockito.when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareReservation()));
    }

    @Test
    public void reservationShouldBeFound() {
        Reservation fromService = reservationService.findByIdOrThrow(anyLong());
        Reservation fromMock = mockService.prepareReservation();
        compareReservations(fromService, fromMock);
    }

    // TODO: 12.12.2021 nice functionality to compare objects - override equals() would be ok?
    private void compareReservations(Reservation s1, Reservation s2) {
        assertEquals(s1.getSeat().getNumber(), s2.getSeat().getNumber());
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> reservationService.findByIdOrThrow(anyLong()));
    }
}
