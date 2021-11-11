package com.example.cinema.service;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.AuditoriumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriumService {
    private final AuditoriumRepository repository;

    public AuditoriumService(AuditoriumRepository repository) {
        this.repository = repository;
    }

    public List<Auditorium> findAll() {
        return repository.findAll();
    }

    public Auditorium findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find auditorium with id: " + id));
    }

    public Auditorium addAuditorium(Auditorium auditorium) {
        return repository.save(auditorium);
    }

    public void deleteAuditorium(Long id) {
        repository.deleteById(id);
    }
}
