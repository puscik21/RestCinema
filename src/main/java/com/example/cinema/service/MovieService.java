package com.example.cinema.service;


import com.example.cinema.entity.Movie;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public List<Movie> findAll() {
        return repository.findAll();
    }

    public Movie findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find movie with id: " + id));
    }

    public Movie addMovie(Movie movie) {
        return repository.save(movie);
    }

    public void deleteMovie(Long id) {
        repository.deleteById(id);
    }
}
