package com.example.cinema.controller;

import com.example.cinema.dto.MovieDTO;
import com.example.cinema.entity.Movie;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@AllArgsConstructor
public class MovieController {

    private final MovieService service;
    private final MappingService mappingService;

    @GetMapping
    public List<MovieDTO> findAll() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MovieDTO getById(@PathVariable Long id) {
        return mappingService.map(service.findByIdOrThrow(id));
    }

    @PostMapping
    public MovieDTO save(@RequestBody @Valid MovieDTO movieDTO) {
        Movie movie = service.save(mappingService.map(movieDTO));
        return mappingService.map(movie);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
