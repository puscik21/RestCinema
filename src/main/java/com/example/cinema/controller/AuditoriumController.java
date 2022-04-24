package com.example.cinema.controller;

import com.example.cinema.dto.AuditoriumDTO;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.service.AuditoriumService;
import com.example.cinema.service.MappingService;
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
@RequestMapping("/auditorium")
@AllArgsConstructor
public class AuditoriumController {

    private final AuditoriumService service;
    private final MappingService mappingService;

    @GetMapping
    public List<AuditoriumDTO> getAllAuditoriums() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AuditoriumDTO getSeatById(@PathVariable Long id) {
        return mappingService.map(service.findByIdOrThrow(id));
    }

    @PostMapping
    public AuditoriumDTO addAuditorium(@RequestBody @Valid AuditoriumDTO auditoriumDTO) {
        Auditorium auditorium = service.addAuditorium(mappingService.map(auditoriumDTO));
        return mappingService.map(auditorium);
    }
}
