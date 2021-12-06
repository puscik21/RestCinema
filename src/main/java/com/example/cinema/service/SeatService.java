package com.example.cinema.service;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeatService {
    private final SeatRepository repository;
    private final AuditoriumService auditoriumService;

    public List<Seat> findAll() {
        return repository.findAll();
    }

    public Seat findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find seat with id: " + id));
    }

    public Seat addSeat(Seat seat) {
        if (seat.getAuditorium().getNumber() == null) {
            throw new RequestException(String.format("Trying to add seat %s without providing auditorum number", seat.getNumber()));
        }
        Auditorium auditorium = auditoriumService.findByNumber(seat.getAuditorium().getNumber());
        if (repository.findSeatByNumber(seat.getNumber()).isPresent()) {
            throw new RequestException(String.format("Seat with number %s already exists in auditorium %s",
                    seat.getNumber(), seat.getAuditorium().getNumber()));
        }
        seat.setAuditorium(auditorium);
        return repository.save(seat);
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
