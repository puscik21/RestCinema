package com.example.cinema.service;

import com.example.cinema.config.MockService;
import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SpectatorServiceTest {

    @Mock
    private SpectatorRepository spectatorRepository;

    private SpectatorService spectatorService;

    private final MockService mockService = new MockService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spectatorService = new SpectatorService(spectatorRepository);
    }

    @Test
    public void spectatorShouldBeFound() {
        Mockito.when(spectatorRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getSpectator()));
        Spectator fromService = spectatorService.findByIdOrThrow(anyLong());
        Spectator fromMock = mockService.getSpectator();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(spectatorRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> spectatorService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void addExistingEmailShouldReturnException() {
        Spectator spectator = mockService.getSpectator();
        when(spectatorRepository.findByEmail(anyString())).thenReturn(Optional.of(spectator));
        assertThrows(RequestException.class, () -> spectatorService.addSpectator(spectator));
    }
}
