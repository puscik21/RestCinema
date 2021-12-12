package com.example.cinema.service;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.AuditoriumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CinemaApplication.class)
public class AuditoriumServiceTest {

    @Mock
    private AuditoriumRepository auditoriumRepository;

    @Autowired
    private MockService mockService;

    private AuditoriumService auditoriumService;

    @BeforeEach
    void setUp() {
        auditoriumRepository = Mockito.mock(AuditoriumRepository.class);
        auditoriumService = new AuditoriumService(auditoriumRepository);
        Mockito.when(auditoriumRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareAuditorium()));
    }

    @Test
    public void auditoriumShouldBeFound() {
        Auditorium fromService = auditoriumService.findByIdOrThrow(anyLong());
        Auditorium fromMock = mockService.prepareAuditorium();
        compareAuditoriums(fromService, fromMock);
    }

    private void compareAuditoriums(Auditorium s1, Auditorium s2) {
        assertEquals(s1.getNumber(), s2.getNumber());
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(auditoriumRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> auditoriumService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void addExistingNumberShouldReturnException() {
        Auditorium auditorium = mockService.prepareAuditorium();
        when(auditoriumRepository.findByNumber(anyInt())).thenReturn(Optional.of(auditorium));
        assertThrows(RequestException.class, () -> auditoriumService.addAuditorium(auditorium));
    }
}
