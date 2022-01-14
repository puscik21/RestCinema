package com.example.cinema.controller;

import com.example.cinema.entity.Movie;
import com.example.cinema.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movies")
@AllArgsConstructor
public class MovieController {
    private final MovieService service;

    @GetMapping
    public List<Movie> getAllMovies() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Movie getSeatById(@PathVariable Long id) {
        return service.findByIdOrThrow(id);
    }

    @PostMapping
    public Movie addMovie(@RequestBody @Valid Movie movie) {
        return service.addMovie(movie);
    }
}
