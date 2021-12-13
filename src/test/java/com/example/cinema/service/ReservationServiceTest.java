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

import static org.assertj.core.api.Assertions.assertThat;
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
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareReservation()));
        when(spectacleService.findById(anyLong())).thenReturn(Optional.of(mockService.prepareSpectacle()));
        when(spectatorService.findById(anyLong())).thenReturn(Optional.of(mockService.prepareSpectator()));
        when(seatService.findById(anyLong())).thenReturn(Optional.of(mockService.prepareSeat()));
    }

    @Test
    public void reservationShouldBeFound() {
        Reservation fromService = reservationService.findByIdOrThrow(anyLong());
        Reservation fromMock = mockService.prepareReservation();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> reservationService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void addExistingReservationShouldReturnException() {
        Reservation reservation = mockService.prepareReservation();
        when(reservationRepository.findByDependenciesIds(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(reservation));
        when(spectatorService.findByIdOrThrow(anyLong())).thenReturn(mockService.prepareSpectator());
        assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
    }

    @Test
    public void addWithNoExistingSpectacleShouldReturnException() {
        Reservation reservation = mockService.prepareReservation();
        when(spectacleService.findById(anyLong())).thenReturn(Optional.empty());
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find spectacle with id:  %s", reservation.getSpectacle().getId()), e.getMessage());
    }

    @Test
    public void addWithNoExistingSpectatorShouldReturnException() {
        Reservation reservation = mockService.prepareReservation();
        when(spectatorService.findById(anyLong())).thenReturn(Optional.empty());
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find spectator with id:  %s", reservation.getSpectator().getId()), e.getMessage());
    }

    @Test
    public void addWithNoExistingSeatShouldReturnException() {
        Reservation reservation = mockService.prepareReservation();
        when(seatService.findById(anyLong())).thenReturn(Optional.empty());
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find seat with id:  %s", reservation.getSeat().getId()), e.getMessage());
    }
}
