package com.example.cinema.service;

import com.example.cinema.MockService;
import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    private ReservationService reservationService;

    private final MockService mockService = new MockService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void reservationShouldBeFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getReservation()));
        Reservation fromService = reservationService.getById(anyLong());
        Reservation fromMock = mockService.getReservation();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    void getNotExistingShouldReturnException() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> reservationService.getById(anyLong()));
    }

    @Test
    public void reservationShouldBeSavedWithBasicConditions() {
        when(seatService.getById(anyLong())).thenReturn(mockService.getSeat());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Reservation reservation = reservationService.save(mockService.getReservation());
        assertNull(reservation.getId());
        assertTrue(reservation.getSeat().isReserved());
    }

    @Test
    void addWithNoExistingSpectacleShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(spectacleService.getById(anyLong())).thenThrow(new RequestException(String.format("Could not find spectacle with id:  %s",
                reservation.getSpectacle().getId())));
        Exception e = assertThrows(RequestException.class, () -> reservationService.save(reservation));
        assertEquals(String.format("Could not find spectacle with id:  %s", reservation.getSpectacle().getId()), e.getMessage());
    }

    @Test
    void addWithNoExistingSpectatorShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(spectatorService.getById(anyLong())).thenThrow(new RequestException(String.format("Could not find spectator with id:  %s"
                , reservation.getSpectator().getId())));
        Exception e = assertThrows(RequestException.class, () -> reservationService.save(reservation));
        assertEquals(String.format("Could not find spectator with id:  %s", reservation.getSpectator().getId()), e.getMessage());
    }

    @Test
    void addWithNoExistingSeatShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        when(seatService.getById(anyLong())).thenThrow(new RequestException(String.format("Could not find seat with id: %s",
                reservation.getSeat().getId())));
        Exception e = assertThrows(RequestException.class, () -> reservationService.save(reservation));
        assertEquals(String.format("Could not find seat with id: %s", reservation.getSeat().getId()), e.getMessage());
    }

    @Test
    void addWithReservedSeatShouldReturnException() {
        Reservation reservation = mockService.getReservation();
        Seat seat = mockService.getSeat();
        seat.setReserved(true);
        when(seatService.getById(anyLong())).thenReturn(seat);
        Exception e = assertThrows(RequestException.class, () -> reservationService.save(reservation));
        assertEquals(String.format("Seat with number %s is already reserved", reservation.getSeat().getNumber()), e.getMessage());
    }
}
