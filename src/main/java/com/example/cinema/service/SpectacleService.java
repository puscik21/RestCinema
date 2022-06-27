package com.example.cinema.service;

import com.example.cinema.entity.Spectacle;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectacleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SpectacleService {

    private final SpectacleRepository repository;

    // TODO: 11.11.2021 make it chronological
    // TODO: 11.11.2021 add pagination
    public List<Spectacle> findAll() {
        log.info("Searching for all spectacles");
        return repository.findAll();
    }

    public Spectacle getById(Long id) throws RequestException {
        log.info("Getting spectacle with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find spectacle with id: " + id));
    }

    public Optional<Spectacle> findById(Long id) throws RequestException {
        log.info("Searching for spectacle with id: {}", id);
        return repository.findById(id);
    }

    public Spectacle save(Spectacle spectacle) {
        // TODO: 6/27/2022 check if date is in future
        spectacle.setId(null);
        spectacle.setReservations(Collections.emptyList());
        log.info("Saving spectacle: {}", spectacle);
        return repository.save(spectacle);
    }

    public Map<String, String> deleteById(Long id) {
        Spectacle spectacle = getById(id);
        spectacle.getReservations()
                .forEach(s -> s.setSpectacle(null));
        log.info("Deleting spectacle with id: {}", id);
        repository.delete(spectacle);
        return Map.of("message", String.format("Spectacle with id: %s has been removed", id));
    }
}
