package com.example.cinema.service;

import com.example.cinema.MockService;
import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SpectacleService spectacleService;

    @Mock
    private SpectatorService spectatorService;

    @Mock
    private SeatService seatService;

    private ReservationService reservationService;

    private final MockService mockService = new MockService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationService = new ReservationService(reservationRepository, spectacleService, spectatorService, seatService);
    }

    @Test
    void reservationShouldBeFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getReservation()));
        Reservation fromService = reservationService.findByIdOrThrow(anyLong());
        Reservation fromMock = mockService.getReservation();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    void getNotExistingShouldReturnException() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> reservationService.findByIdOrThrow(anyLong()));
    }

    @Test
    void addWithNoExistingSpectacleShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(spectacleService.findByIdOrThrow(anyLong())).thenThrow(new RequestException(String.format("Could not find spectacle with id:  %s",
                reservation.getSpectacle().getId())));
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find spectacle with id:  %s", reservation.getSpectacle().getId()), e.getMessage());
    }

    @Test
    void addWithNoExistingSpectatorShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(spectatorService.findByIdOrThrow(anyLong())).thenThrow(new RequestException(String.format("Could not find spectator with id:  %s"
                , reservation.getSpectator().getId())));
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find spectator with id:  %s", reservation.getSpectator().getId()), e.getMessage());
    }

    @Test
    void addWithNoExistingSeatShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(seatService.findByIdOrThrow(anyLong())).thenThrow(new RequestException(String.format("Could not find seat with id: %s",
                reservation.getSeat().getId())));
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Could not find seat with id: %s", reservation.getSeat().getId()), e.getMessage());
    }

    @Test
    void addWithReservedSeatShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        Seat seat = mockService.getSeat();
        seat.setReserved(true);
        when(seatService.findByIdOrThrow(anyLong())).thenReturn(seat);
        Exception e = assertThrows(RequestException.class, () -> reservationService.addReservation(reservation));
        assertEquals(String.format("Seat with number %s is already reserved", reservation.getSeat().getNumber()), e.getMessage());
    }
}
