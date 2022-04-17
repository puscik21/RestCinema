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

    private MockService mockService;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        mockService = new MockService();
        reservationRepository = Mockito.mock(ReservationRepository.class);
        spectacleService = Mockito.mock(SpectacleService.class);
        spectatorService = Mockito.mock(SpectatorService.class);
        seatService = Mockito.mock(SeatService.class);
        reservationService = new ReservationService(reservationRepository, spectacleService, spectatorService, seatService);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getReservation()));
        when(spectacleService.findById(anyLong())).thenReturn(Optional.of(mockService.getSpectacle()));
        when(spectatorService.findById(anyLong())).thenReturn(Optional.of(mockService.getSpectator()));
        when(seatService.findById(anyLong())).thenReturn(Optional.of(mockService.getSeat()));
    }

    @Test
    public void reservationShouldBeFound() {
        Reservation fromService = reservationService.findByIdOrThrow(anyLong());
        Reservation fromMock = mockService.getReservation();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> reservationService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void addWithNoExistingSpectacleShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(spectacleService.findById(anyLong())).thenReturn(Optional.empty());
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find spectacle with id:  %s", reservation.getSpectacle().getId()), e.getMessage());
    }

    @Test
    public void addWithNoExistingSpectatorShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(spectatorService.findById(anyLong())).thenReturn(Optional.empty());
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find spectator with id:  %s", reservation.getSpectator().getId()), e.getMessage());
    }

    @Test
    public void addWithNoExistingSeatShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(seatService.findById(anyLong())).thenReturn(Optional.empty());
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find seat with id:  %s", reservation.getSeat().getId()), e.getMessage());
    }

    // TODO: 4/17/2022 check if seat is reserved
}
