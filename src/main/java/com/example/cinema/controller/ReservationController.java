package com.example.cinema.controller;

import com.example.cinema.dto.ReservationDTO;
import com.example.cinema.entity.Reservation;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.ReservationService;
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
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService service;
    private final MappingService mappingService;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReservationDTO getSeatById(@PathVariable Long id) {
        return mappingService.map(service.findByIdOrThrow(id));
    }

    @PostMapping
    public Reservation addReservation(@RequestBody @Valid Reservation reservation) {
        return service.addReservation(reservation);
    }
}
