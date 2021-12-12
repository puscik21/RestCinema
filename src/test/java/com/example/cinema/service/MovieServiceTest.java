package com.example.cinema.service;

import com.example.cinema.CinemaApplication;
import com.example.cinema.MockService;
import com.example.cinema.entity.Movie;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.MovieRepository;
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
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Autowired
    private MockService mockService;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieRepository = Mockito.mock(MovieRepository.class);
        movieService = new MovieService(movieRepository);
        Mockito.when(movieRepository.findById(anyLong())).thenReturn(Optional.of(mockService.prepareMovie()));
    }

    @Test
    public void movieShouldBeFound() {
        Movie fromService = movieService.findByIdOrThrow(anyLong());
        Movie fromMock = mockService.prepareMovie();
        compareMovies(fromService, fromMock);
    }

    private void compareMovies(Movie m1, Movie m2) {
        assertEquals(m1.getName(), m2.getName());
        assertEquals(m1.getSpectacles(), m2.getSpectacles());
    }

    @Test
    public void getNotExistingShouldReturnException() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> movieService.findByIdOrThrow(anyLong()));
    }
}
