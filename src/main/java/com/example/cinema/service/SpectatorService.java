package com.example.cinema.service;

import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        repository.deleteById(id);
        return Map.of("message", String.format("Spectator with id: %s has been removed", id));
    }
}
