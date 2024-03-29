package com.example.cinema.service;

import com.example.cinema.entity.Movie;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository repository;

    public List<Movie> findAll() {
        log.info("Searching for all movies");
        return repository.findAll();
    }

    public Movie getById(Long id) throws RequestException {
        log.info("Getting movie with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find movie with id: " + id));
    }

    public Optional<Movie> findById(Long id) throws RequestException {
        log.info("Searching for movie with id: {}", id);
        return repository.findById(id);
    }

    public Movie save(Movie movie) {
        movie.setId(null);
        movie.setSpectacles(Collections.emptyList());
        log.info("Saving movie: {}", movie);
        return repository.save(movie);
    }

    public Map<String, String> deleteById(Long id) {
        Movie movie = getById(id);
        movie.getSpectacles()
                .forEach(spectacle -> spectacle.setMovie(null));
        log.info("Deleting movie with name: {}", movie.getName());
        repository.delete(movie);
        return Map.of("message", String.format("Movie with name: %s has been removed", movie.getName()));
    }
}
