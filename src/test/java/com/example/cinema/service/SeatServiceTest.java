package com.example.cinema.service;

import com.example.cinema.config.MockService;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private AuditoriumService auditoriumService;

    private SeatService seatService;

    private final MockService mockService = new MockService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seatService = new SeatService(seatRepository, auditoriumService);
    }

    @Test
    public void seatShouldBeFound() {
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getSeat()));
        Seat fromService = seatService.findByIdOrThrow(anyLong());
        Seat fromMock = mockService.getSeat();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(seatRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> seatService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void reservationForReservedShouldReturnException() {
        Seat seat = mockService.getSeat();
        seat.setReserved(true);
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(seat));
        assertThrows(RequestException.class, () -> seatService.changeReservedState(anyLong(), true));
    }

    @Test
    public void addExistingNumberInAuditoriumShouldReturnException() {
        Seat seat = mockService.getSeat();
        when(auditoriumService.findByIdOrThrow(anyLong())).thenReturn(mockService.getAuditorium());
        when(seatRepository.findSeatByNumber(anyInt())).thenReturn(Optional.of(mockService.getSeat()));
        Exception e = assertThrows(RequestException.class, () -> seatService.addSeat(seat));
        assertEquals(String.format("Seat with number %s already exists in auditorium number %s",
                seat.getNumber(), seat.getAuditorium().getNumber()), e.getMessage());
    }

    @Test
    public void addSeatWithoutAuditoriumNumberShouldReturnException() {
        Seat seat = mockService.getSeat();
        when(auditoriumService.findByIdOrThrow(anyLong())).thenThrow(new RequestException(String.format("Could not find auditorium with id: %s",
                seat.getAuditorium().getId())));
        Exception e = assertThrows(RequestException.class, () -> seatService.addSeat(seat));
        assertEquals(String.format("Could not find auditorium with id: %s", seat.getAuditorium().getId()), e.getMessage());
    }

}
