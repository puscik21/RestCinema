package com.example.cinema.service;

import com.example.cinema.entity.Seat;
import com.example.cinema.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository repository;

    public SeatService(SeatRepository repository) {
        this.repository = repository;
    }

    public List<Seat> findAll() {
        return repository.findAll();
    }

    public Seat addSeat(Seat seat) {
        return repository.save(seat);
    }

    public Seat changeTakenState(Long id, boolean isTaken) throws IllegalArgumentException {
        Seat seat = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Cannot find seat with id:" + id));
        seat.setTaken(isTaken);
        return repository.save(seat);
    }

    public void deleteSeat(Long id) {
        repository.deleteById(id);
    }
}
