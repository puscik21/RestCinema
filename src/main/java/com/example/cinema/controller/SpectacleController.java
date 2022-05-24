package com.example.cinema.controller;

import com.example.cinema.dto.SpectacleDTO;
import com.example.cinema.entity.Spectacle;
import com.example.cinema.service.MappingService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spectacles")
@AllArgsConstructor
public class SpectacleController {

    private final SpectacleService service;
    private final MappingService mappingService;

    @GetMapping
    public List<SpectacleDTO> findAll() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SpectacleDTO getById(@PathVariable Long id) {
        return mappingService.map(service.findByIdOrThrow(id));
    }

    @PostMapping
    public SpectacleDTO save(@RequestBody @Valid SpectacleDTO spectacleDTO) {
        Spectacle spectacle = service.save(mappingService.map(spectacleDTO));
        return mappingService.map(spectacle);
    }
}
