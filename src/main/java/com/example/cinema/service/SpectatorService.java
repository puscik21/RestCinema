package com.example.cinema.service;

import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectatorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public List<Spectator> findAll() {
        log.info("Searching for all spectators");
        return repository.findAll();
    }

    public Spectator getById(Long id) throws RequestException {
        log.info("Getting spectator with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find spectator with id: " + id));
    }

    public Optional<Spectator> findById(Long id) throws RequestException {
        log.info("Searching for spectator with id: {}", id);
        return repository.findById(id);
    }

    public Spectator save(Spectator spectator) {
        if (repository.findByEmail(spectator.getEmail()).isPresent()){
            throw new RequestException(String.format("Spectator with email %s already exists", spectator.getEmail()));
        }
        spectator.setId(null);
        spectator.setReservations(Collections.emptyList());
        spectator.setPassword(passwordEncoder.encode(spectator.getPassword()));
        log.info("Saving spectator: {}", spectator);
        return repository.save(spectator);
    }

    public Map<String, String> deleteById(Long id) {
        Spectator spectator = getById(id);
        spectator.getReservations()
                .forEach(s -> s.setSpectator(null));
        log.info("Deleting spectator with id: {}", id);
        repository.delete(spectator);
        return Map.of("message", String.format("Spectator with id: %s has been removed", id));
    }
}
