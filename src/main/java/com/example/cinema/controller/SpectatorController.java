package com.example.cinema.controller;

import com.example.cinema.dto.SpectatorDTO;
import com.example.cinema.entity.Spectator;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.SpectatorService;
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
@RequestMapping("/spectators")
@AllArgsConstructor
public class SpectatorController {

    private final SpectatorService service;
    private final MappingService mappingService;

    @GetMapping
    public List<SpectatorDTO> findAll() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SpectatorDTO getById(@PathVariable Long id) {
        return mappingService.map(service.getById(id));
    }

    // TODO: 6/27/2022 probably to be removed
    @PostMapping
    public SpectatorDTO save(@RequestBody @Valid SpectatorDTO spectatorDTO) {
        Spectator spectator = service.save(mappingService.map(spectatorDTO));
        return mappingService.map(spectator);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
