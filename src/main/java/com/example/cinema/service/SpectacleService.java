package com.example.cinema.service;

import com.example.cinema.entity.Spectacle;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectacleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SpectacleService {

    private final SpectacleRepository repository;

    // TODO: 11.11.2021 make it chronological
    // TODO: 11.11.2021 add pagination
    // TODO: 11.11.2021 provide only basic info like movie, hour (not the whole auditorium)
    public List<Spectacle> findAll() {
        return repository.findAll();
    }

    public Spectacle findByIdOrThrow(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find spectacle with id: " + id));
    }

    public Optional<Spectacle> findById(Long id) throws RequestException {
        return repository.findById(id);
    }

    public Spectacle addSpectacle(Spectacle spectacle) {
        spectacle.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)); // TODO: 4/16/2022 @Valid for date
        spectacle.setId(null);
        spectacle.setReservations(Collections.emptyList());
        return repository.save(spectacle);
    }

    public void deleteSpectacle(Long id) {
        repository.deleteById(id);
    }
}
