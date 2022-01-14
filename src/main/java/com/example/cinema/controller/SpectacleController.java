package com.example.cinema.controller;

import com.example.cinema.entity.Spectacle;
import com.example.cinema.service.SpectacleService;
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
@RequestMapping("/spectacles")
@AllArgsConstructor
public class SpectacleController {
    private final SpectacleService service;

    @GetMapping
    public List<Spectacle> getAllSpectacles() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Spectacle getSeatById(@PathVariable Long id) {
        return service.findByIdOrThrow(id);
    }

    @PostMapping
    public Spectacle addSpectacle(@RequestBody @Valid Spectacle spectacle) {
        return service.addSpectacle(spectacle);
    }
}
