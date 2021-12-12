package com.example.cinema.service;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Spectacle;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectacleRepository;
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
public class SpectacleServiceTest {

    @Mock
    private SpectacleRepository spectacleRepository;

    @Autowired
    private MockService mockService;

    private SpectacleService spectacleService;

    @BeforeEach
    void setUp() {
        spectacleRepository = Mockito.mock(SpectacleRepository.class);
        spectacleService = new SpectacleService(spectacleRepository);
        Mockito.when(spectacleRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareSpectacle()));
    }

    @Test
    public void spectacleShouldBeFound() {
        Spectacle fromService = spectacleService.findByIdOrThrow(anyLong());
        Spectacle fromMock = mockService.prepareSpectacle();
        compareSpectacles(fromService, fromMock);
    }

    private void compareSpectacles(Spectacle s1, Spectacle s2) {
        assertEquals(s1.getDateTime(), s2.getDateTime());
        assertEquals(s1.getMovie(), s2.getMovie());
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(spectacleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> spectacleService.findByIdOrThrow(anyLong()));
    }
}
