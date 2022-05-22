package com.example.cinema.service;

import com.example.cinema.MockService;
import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SpectatorServiceTest {

    @Mock
    private SpectatorRepository spectatorRepository;

    @InjectMocks
    private SpectatorService spectatorService;

    private final MockService mockService = new MockService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    public void spectatorShouldBeSavedWithBasicConditions() {
        when(spectatorRepository.save(any(Spectator.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Spectator spectator = spectatorService.save(mockService.getSpectator());
        assertNull(spectator.getId());
        assertEquals(Collections.emptyList(), spectator.getReservations());
    }

    @Test
    public void addExistingEmailShouldReturnException() {
        Spectator spectator = mockService.getSpectator();
        when(spectatorRepository.findByEmail(anyString())).thenReturn(Optional.of(spectator));
        assertThrows(RequestException.class, () -> spectatorService.save(spectator));
    }
}
