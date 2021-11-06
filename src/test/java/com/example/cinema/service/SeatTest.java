package com.example.cinema.service;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SeatRepository;
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
class SeatTest {

    @Mock
    private SeatRepository seatRepository;

    @Autowired
    private MockService mockService;

    private SeatService seatService;

    @BeforeEach
    void setUp() {
        seatRepository = Mockito.mock(SeatRepository.class);
        seatService = new SeatService(seatRepository);
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareSeat()));
    }

    @Test
    public void seatShouldBeFound() {
        Seat fromService = seatService.findById(anyLong());
        Seat fromMock = mockService.prepareSeat();
        compareSeats(fromService, fromMock);
    }

    private void compareSeats(Seat s1, Seat s2) {
        assertEquals(s1.getNumber(), s2.getNumber());
        assertEquals(s1.isReserved(), s2.isReserved());
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(seatRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> seatService.findById(anyLong()));
    }

    @Test
    public void reservationForReservedShouldReturnException() {
        Seat seat = mockService.prepareSeat();
        seat.setReserved(true);
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(seat));
        assertThrows(RequestException.class, () -> seatService.changeReservedState(anyLong(), true));
    }
}