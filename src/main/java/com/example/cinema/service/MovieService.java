package com.example.cinema.service;

import com.example.cinema.entity.Movie;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository repository;

    public List<Movie> findAll() {
        return repository.findAll();
    }

    public Movie findByIdOrThrow(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find movie with id: " + id));
    }

    public Optional<Movie> findById(Long id) throws RequestException {
        return repository.findById(id);
    }

    public Movie addMovie(Movie movie) {
        movie.setId(null);
        return repository.save(movie);
    }

    public void deleteMovie(Long id) {
        repository.deleteById(id);
    }
}
