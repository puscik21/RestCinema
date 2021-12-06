package com.example.cinema.service;

import com.example.cinema.entity.Spectacle;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.SpectacleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SpectacleService {

    private final SpectacleRepository repository;
    private final SeatService seatService;

    // TODO: 11.11.2021 make it chronological
    // TODO: 11.11.2021 add pagination
    // TODO: 11.11.2021 provide only basic info like movie, hour (not the whole auditorium)
    public List<Spectacle> findAll() {
        return repository.findAll();
    }

    public Spectacle findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find seat with id: " + id));
    }

    public Spectacle addSpectacle(Spectacle spectacle) {
        return repository.save(spectacle);
    }

    public void deleteSpectacle(Long id) {
        repository.deleteById(id);
    }
}
