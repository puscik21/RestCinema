package com.example.cinema.service;

import com.example.cinema.entity.Spectacle;
import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectatorRepository;
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
public class SpectatorService {
    private final SpectatorRepository repository;

    public List<Spectator> findAll() {
        return repository.findAll();
    }

    public Spectator findByIdOrThrow(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find spectator with id: " + id));
    }

    public Optional<Spectator> findById(Long id) throws RequestException {
        return repository.findById(id);
    }

    public Spectator save(Spectator spectator) {
        if (repository.findByEmail(spectator.getEmail()).isPresent()){
            throw new RequestException(String.format("Spectator with email %s already exists", spectator.getEmail()));
        }
        spectator.setId(null);
        spectator.setReservations(Collections.emptyList());
        return repository.save(spectator);
    }

    public Map<String, String> deleteById(Long id) {
        Spectator spectator = findByIdOrThrow(id);
        spectator.getReservations()
                .forEach(s -> s.setSpectator(null));
        log.info("Deleting spectator with id: {}", id);
        repository.delete(spectator);
        return Map.of("message", String.format("Spectator with id: %s has been removed", id));
    }
}
