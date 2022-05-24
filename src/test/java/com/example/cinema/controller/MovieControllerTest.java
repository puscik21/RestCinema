package com.example.cinema.controller;

import com.example.cinema.MockService;
import com.example.cinema.config.TestConfig;
import com.example.cinema.dto.MovieDTO;
import com.example.cinema.entity.Movie;
import com.example.cinema.exception.RequestExceptionHandler;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.MovieService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(controllers = MovieController.class)
class MovieControllerTest {

    @MockBean
    private MovieService movieService;

    @Autowired
    private MovieController controller;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockService mockService;

    @Autowired
    private RequestExceptionHandler requestExceptionHandler;

    private MockMvc mockMvc;

    private final String MOVIES_PATH = "/movies";

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(requestExceptionHandler)
                .build();
    }

    @Test
    void shouldFindAll() throws Exception {
        List<Movie> mockedMovies = mockService.prepareMoviesList();
        when(movieService.findAll()).thenReturn(mockedMovies);
        List<MovieDTO> expected = mapToDto(mockedMovies);
        MvcResult result = mockMvc.perform(get(MOVIES_PATH))
                .andExpect(status().isOk())
                .andReturn();
        List<MovieDTO> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private List<MovieDTO> mapToDto(List<Movie> movies) {
        return movies.stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @Test
    void movieShouldBeAdded() throws Exception {
        Movie movie = mockService.getMovie();
        when(movieService.save(any(Movie.class))).thenReturn(movie);
        MovieDTO movieDTO = mappingService.map(movie);
        String body = objectMapper.writeValueAsString(movieDTO);
        mockMvc.perform(post(MOVIES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void savingWithoutNameShouldReturn400Status() throws Exception {
        MovieDTO movieDTO = mappingService.map(mockService.getMovie());
        movieDTO.setName(null);
        check400StatusForValidationException(movieDTO);
    }

    private void check400StatusForValidationException(MovieDTO movieDTO) throws Exception {
        String body = objectMapper.writeValueAsString(movieDTO);
        mockMvc.perform(post(MOVIES_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(status().isBadRequest());
    }
}