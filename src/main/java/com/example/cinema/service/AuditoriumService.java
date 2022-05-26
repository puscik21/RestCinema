package com.example.cinema.service;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.AuditoriumRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AuditoriumService {
    private final AuditoriumRepository repository;

    public List<Auditorium> findAll() {
        log.info("Searching for all auditoriums");
        return repository.findAll();
    }

    public Auditorium getById(Long id) throws RequestException {
        log.info("Getting auditorium with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find auditorium with id: " + id));
    }

    public Optional<Auditorium> findById(Long id) throws RequestException {
        log.info("Searching for auditorium with id: {}", id);
        return repository.findById(id);
    }

    public Auditorium save(Auditorium auditorium) {
        if (repository.findByNumber(auditorium.getNumber()).isPresent()) {
            throw new RequestException(String.format("Auditorium with number %s already exists", auditorium.getNumber()));
        }
        auditorium.setId(null);
        auditorium.setSpectacles(Collections.emptyList());
        log.info("Saving auditorium: {}", auditorium);
        return repository.save(auditorium);
    }

    public Map<String, String> deleteById(Long id) {
        Auditorium auditorium = getById(id);
        auditorium.getSeats()
                .forEach(seat -> seat.getReservations()
                        .forEach(reservation -> reservation.setSeat(null)));
        auditorium.getSpectacles()
                .forEach(spectacle -> spectacle.setAuditorium(null));
        log.info("Deleting auditorium with number: {}", auditorium.getNumber());
        repository.delete(auditorium);
        return Map.of("message", String.format("Auditorium with number: %s has been removed", auditorium.getNumber()));
    }
}
