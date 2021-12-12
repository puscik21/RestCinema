package com.example.cinema.controller;

import com.example.cinema.entity.Spectator;
import com.example.cinema.service.SpectatorService;
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
@RequestMapping("/spectators")
@AllArgsConstructor
public class SpectatorController {
    private final SpectatorService service;

    @GetMapping
    public List<Spectator> getAllSpectators() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Spectator getSeatById(@PathVariable Long id) {
        return service.findByIdOrThrow(id);
    }

    @PostMapping
    public Spectator addSpectator(@RequestBody @Valid Spectator spectator) {
        return service.addSpectator(spectator);
    }
}
