package com.example.cinema.controller;

import com.example.cinema.entity.Seat;
import com.example.cinema.service.SeatService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Seat addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }

    @PutMapping("/{id}")
    public Seat changeTakenState(@PathVariable Long id, @RequestBody Seat seat) {
        return seatService.changeTakenState(id, seat.isTaken());
    }

    @DeleteMapping("/{id}")
    public String deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return "Seat was deleted";
    }
}
