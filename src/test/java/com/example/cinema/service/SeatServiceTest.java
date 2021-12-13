package com.example.cinema.service;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.AuditoriumRepository;
import com.example.cinema.repository.SeatRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CinemaApplication.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private MockService mockService;

    private SeatService seatService;
    private AuditoriumService auditoriumService;

    @BeforeEach
    void setUp() {
        seatRepository = Mockito.mock(SeatRepository.class);
        auditoriumRepository = Mockito.mock(AuditoriumRepository.class);
        auditoriumService = new AuditoriumService(auditoriumRepository);
        seatService = new SeatService(seatRepository, auditoriumService);
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareSeat()));
        when(seatRepository.findSeatByNumber(anyInt())).thenReturn(Optional.of(mockService.prepareSeat()));
    }

    @Test
    public void seatShouldBeFound() {
        Seat fromService = seatService.findByIdOrThrow(anyLong());
        Seat fromMock = mockService.prepareSeat();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(seatRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> seatService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void reservationForReservedShouldReturnException() {
        Seat seat = mockService.prepareSeat();
        seat.setReserved(true);
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(seat));
        assertThrows(RequestException.class, () -> seatService.changeReservedState(anyLong(), true));
    }

    @Test
    public void addExistingNumberInAuditoriumShouldReturnException() {
        Seat seat = mockService.prepareSeat();
        when(auditoriumRepository.findByNumber(anyInt())).thenReturn(Optional.of(mockService.prepareAuditoriumWithSeat()));
        Exception e = assertThrows(RequestException.class, () -> seatService.addSeat(seat));
        assertEquals(String.format("Seat with number %s already exists in auditorium %s",
                seat.getNumber(), seat.getAuditorium().getNumber()), e.getMessage());
    }

    @Test
    public void addSeatWithoutAuditoriumNumberShouldReturnException() {
        Seat seat = mockService.prepareSeat();
        seat.getAuditorium().setNumber(null);
        Exception e = assertThrows(RequestException.class, () -> seatService.addSeat(seat));
        assertEquals(String.format("Trying to add seat %s without providing auditorum number", seat.getNumber()), e.getMessage());
    }

}