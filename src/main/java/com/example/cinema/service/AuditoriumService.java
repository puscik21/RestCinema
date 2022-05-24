package com.example.cinema.service;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.AuditoriumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuditoriumService {
    private final AuditoriumRepository repository;

    public List<Auditorium> findAll() {
        return repository.findAll();
    }

    public Auditorium findByIdOrThrow(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find auditorium with id: " + id));
    }

    public Optional<Auditorium> findById(Long id) throws RequestException {
        return repository.findById(id);
    }

    public Auditorium save(Auditorium auditorium) {
        if (repository.findByNumber(auditorium.getNumber()).isPresent()) {
            throw new RequestException(String.format("Auditorium with number %s already exists", auditorium.getNumber()));
        }
        auditorium.setId(null);
        auditorium.setSpectacles(Collections.emptyList());
        return repository.save(auditorium);
    }

    public Map<String, String> deleteById(Long id) {
        repository.deleteById(id);
        return Map.of("message", String.format("Auditorium with id: %s has been removed", id));
    }
}
