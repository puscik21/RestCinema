package com.example.cinema.service;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectatorRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CinemaApplication.class)
public class SpectatorServiceTest {

    @Mock
    private SpectatorRepository spectatorRepository;

    @Autowired
    private MockService mockService;

    private SpectatorService spectatorService;

    @BeforeEach
    void setUp() {
        spectatorRepository = Mockito.mock(SpectatorRepository.class);
        spectatorService = new SpectatorService(spectatorRepository);
        Mockito.when(spectatorRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareSpectator()));
    }

    @Test
    public void spectatorShouldBeFound() {
        Spectator fromService = spectatorService.findByIdOrThrow(anyLong());
        Spectator fromMock = mockService.prepareSpectator();
        compareSpectators(fromService, fromMock);
    }

    private void compareSpectators(Spectator s1, Spectator s2) {
        assertEquals(s1.getEmail(), s2.getEmail());
        assertEquals(s1.getName(), s2.getName());
        assertEquals(s1.getPhoneNumber(), s2.getPhoneNumber());
        assertEquals(s1.getReservations(), s2.getReservations());
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(spectatorRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> spectatorService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void addExistingEmailShouldReturnException() {
        Spectator spectator = mockService.prepareSpectator();
        when(spectatorRepository.findByEmail(anyString())).thenReturn(Optional.of(spectator));
        assertThrows(RequestException.class, () -> spectatorService.addSpectator(spectator));
    }
}
