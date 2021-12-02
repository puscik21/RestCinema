package com.example.cinema.controller;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.service.AuditoriumService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auditorium")
@AllArgsConstructor
public class AuditoriumController {

    private final AuditoriumService service;

    @GetMapping
    public List<Auditorium> getAllAuditoriums() {
        return service.findAll();
    }

    @PostMapping
    public Auditorium addAuditorium(@RequestBody @Valid Auditorium auditorium) {
        return service.addAuditorium(auditorium);
    }
}
