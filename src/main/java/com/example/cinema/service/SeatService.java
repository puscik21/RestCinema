package com.example.cinema.service;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeatService {
    private final SeatRepository repository;
    private final AuditoriumService auditoriumService;

    public List<Seat> findAll() {
        return repository.findAll();
    }

    public Seat findByIdOrThrow(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find seat with id: " + id));
    }

    public Optional<Seat> findById(Long id) throws RequestException {
        return repository.findById(id);
    }

    public Seat save(Seat seat) {
        Auditorium auditorium = auditoriumService.findByIdOrThrow(seat.getAuditorium().getId());
        if (repository.findByNumber(seat.getNumber()).isPresent()) {
            throw new RequestException(String.format("Seat with number %s already exists in auditorium number %s",
                    seat.getNumber(), auditorium.getNumber()));
        }
        seat.setId(null);
        seat.setAuditorium(auditorium);
        seat.setReservations(Collections.emptyList());
        return repository.save(seat);
    }

    public Seat changeReservedState(Long id, boolean isReserved) throws IllegalArgumentException {
        Seat seat = findByIdOrThrow(id);
        if (isReserved && seat.isReserved()) {
            throw new RequestException(String.format("Seat %s is already reserved", id));
        }
        seat.setReserved(isReserved);
        return repository.save(seat);
    }

    public Map<String, String> deleteById(Long id) {
        repository.deleteById(id);
        return Map.of("message", String.format("Seat with id: %s has been removed", id));
    }
}
