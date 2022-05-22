package com.example.cinema.service;

import com.example.cinema.MockService;
import com.example.cinema.entity.Spectacle;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectacleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
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
import static org.mockito.Mockito.when;

public class SpectacleServiceTest {

    @Mock
    private SpectacleRepository spectacleRepository;

    private SpectacleService spectacleService;

    private final MockService mockService = new MockService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spectacleService = new SpectacleService(spectacleRepository);
    }

    @Test
    public void spectacleShouldBeFound() {
        Mockito.when(spectacleRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getSpectacle()));
        Spectacle fromService = spectacleService.findByIdOrThrow(anyLong());
        Spectacle fromMock = mockService.getSpectacle();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(spectacleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> spectacleService.findByIdOrThrow(anyLong()));
    }

    @Test
    public void spectacleShouldBeSavedWithBasicConditions() {
        when(spectacleRepository.save(any(Spectacle.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        Spectacle spectacle = spectacleService.save(mockService.getSpectacle());
        assertNull(spectacle.getId());
        assertEquals(Collections.emptyList(), spectacle.getReservations());
    }
}
