package com.example.cinema.service;

import com.example.cinema.config.MockService;
import com.example.cinema.entity.Movie;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    private final MockService mockService = new MockService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movieService = new MovieService(movieRepository);
    }

    @Test
    public void movieShouldBeFound() {
        Mockito.when(movieRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getMovie()));
        Movie fromService = movieService.findByIdOrThrow(anyLong());
        Movie fromMock = mockService.getMovie();
        assertThat(fromService).usingRecursiveComparison().isEqualTo(fromMock);
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> movieService.findByIdOrThrow(anyLong()));
    }
}
