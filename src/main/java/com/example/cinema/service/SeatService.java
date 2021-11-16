package com.example.cinema.service;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository repository;
    private final AuditoriumService auditoriumService;

    public SeatService(SeatRepository repository, AuditoriumService auditoriumService) {
        this.repository = repository;
        this.auditoriumService = auditoriumService;
    }

    public List<Seat> findAll() {
        return repository.findAll();
    }

    public Seat findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find seat with id: " + id));
    }

    // TODO: 16.11.2021 improve error handling message
    public Seat addSeat(Seat seat) {
        Auditorium auditorium = auditoriumService.findById(seat.getAuditorium().getId());
        seat.setAuditorium(auditorium);
        if (checkIfNumberExistsInAuditorium(seat)) {
            throw new RequestException(String.format("Seat with number %s already exists in auditorium %s", seat.getNumber(), auditorium.getNumber()));
        }
        return repository.save(seat);
    }

    // TODO: 12.11.2021 make some nice optimal query
    private boolean checkIfNumberExistsInAuditorium(Seat seat) {
        return repository.findAll().stream().anyMatch(
                s -> s.getNumber() == seat.getNumber() && s.getAuditorium().getNumber() == seat.getAuditorium().getNumber()
        );
    }

    public void deleteSeat(Long id) {
        repository.deleteById(id);
    }

    public Seat changeReservedState(Long id, boolean isReserved) throws IllegalArgumentException {
        Seat seat = findById(id);
        if (isReserved && seat.isReserved()) {
            throw new RequestException(String.format("Seat %s is already reserved", id));
        }
        seat.setReserved(isReserved);
        return repository.save(seat);
    }
}
