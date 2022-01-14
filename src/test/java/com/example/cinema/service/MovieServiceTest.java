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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CinemaApplication.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MockService mockService;
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        mockService = new MockService();
        movieRepository = Mockito.mock(MovieRepository.class);
        movieService = new MovieService(movieRepository);
        Mockito.when(movieRepository.findById(anyLong())).thenReturn(Optional.of(mockService.getMovie()));
    }

    @Test
    public void movieShouldBeFound() {
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
