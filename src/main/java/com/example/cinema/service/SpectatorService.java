package com.example.cinema.service;

import com.example.cinema.entity.Spectator;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpectatorService {
    private final SpectatorRepository repository;

    public SpectatorService(SpectatorRepository repository) {
        this.repository = repository;
    }

    public List<Spectator> findAll() {
        return repository.findAll();
    }

    public Spectator findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find spectator with id: " + id));
    }

    public Spectator addSpectator(Spectator spectator) {
        return repository.save(spectator);
    }

    public void deleteSpectator(Long id) {
        repository.deleteById(id);
    }
}
