package com.example.cinema.service;

import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
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

    public Seat findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find seat with id: " + id));
    }

    public Seat addSeat(Seat seat) {
        return repository.save(seat);
    }

    public Seat changeReservedState(Long id, boolean isReserved) throws IllegalArgumentException {
        Seat seat = findById(id);
        if (isReserved && seat.isReserved()) {
            throw new RequestException(String.format("Seat %s is already reserved", id));
        }
        seat.setReserved(isReserved);
        return repository.save(seat);
    }

    public void deleteSeat(Long id) {
        repository.deleteById(id);
    }
}
