package com.example.cinema.controller;

import com.example.cinema.entity.Reservation;
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

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService service;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Reservation getSeatById(@PathVariable Long id) {
        return service.findByIdOrThrow(id);
    }

    @PostMapping
    public Reservation addReservation(@RequestBody @Valid Reservation reservation) {
        return service.addReservation(reservation);
    }
}
