package com.example.cinema.controller;

import com.example.cinema.dto.AuditoriumDTO;
import com.example.cinema.entity.Auditorium;
import com.example.cinema.service.AuditoriumService;
import com.example.cinema.service.MappingService;
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
@RequestMapping("/auditoriums")
@AllArgsConstructor
public class AuditoriumController {

    private final AuditoriumService service;
    private final MappingService mappingService;

    @GetMapping
    public List<AuditoriumDTO> findAll() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AuditoriumDTO getById(@PathVariable Long id) {
        return mappingService.map(service.getById(id));
    }

    @PostMapping
    public AuditoriumDTO save(@RequestBody @Valid AuditoriumDTO auditoriumDTO) {
        Auditorium auditorium = service.save(mappingService.map(auditoriumDTO));
        return mappingService.map(auditorium);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
