package com.example.cinema.controller;

import com.example.cinema.dto.ReservationDTO;
import com.example.cinema.entity.Reservation;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.ReservationService;
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
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService service;
    private final MappingService mappingService;

    @GetMapping
    public List<ReservationDTO> findAll() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReservationDTO getById(@PathVariable Long id) {
        return mappingService.map(service.findByIdOrThrow(id));
    }

    @PostMapping
    public ReservationDTO save(@RequestBody @Valid ReservationDTO reservationDTO) {
        Reservation reservation = service.save(mappingService.map(reservationDTO));
        return mappingService.map(reservation);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
