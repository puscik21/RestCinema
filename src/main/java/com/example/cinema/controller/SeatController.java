package com.example.cinema.controller;

import com.example.cinema.dto.SeatDTO;
import com.example.cinema.entity.Seat;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seats")
@AllArgsConstructor
public class SeatController {

    private final SeatService service;
    private final MappingService mappingService;

    @GetMapping
    public List<SeatDTO> findAll() {
        return service.findAll().stream()
                .map(mappingService::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SeatDTO getById(@PathVariable Long id) {
        return mappingService.map(service.findByIdOrThrow(id));
    }

    @PostMapping
    public SeatDTO save(@RequestBody @Valid SeatDTO seatDTO) {
        Seat seat = service.save(mappingService.map(seatDTO));
        return mappingService.map(seat);
    }

    // TODO: 4/16/2022 PUT/PATCH method
    @PutMapping("/{id}")
    public SeatDTO changeReservedState(@PathVariable Long id, @RequestBody SeatDTO seatDTO) {
        Seat seat = service.changeReservedState(id, seatDTO.isReserved());
        return mappingService.map(seat);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Seat was deleted";
    }
}
