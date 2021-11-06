package com.example.cinema.controller;

import com.example.cinema.entity.Seat;
import com.example.cinema.service.SeatService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.findAll();
    }

    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable Long id) {
        return seatService.findById(id);
    }

    @PostMapping
    public Seat addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }

    @PutMapping("/{id}")
    public Seat changeReservedState(@PathVariable Long id, @RequestBody Seat seat) {
        return seatService.changeReservedState(id, seat.isReserved());
    }

    @DeleteMapping("/{id}")
    public String deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return "Seat was deleted";
    }
}
