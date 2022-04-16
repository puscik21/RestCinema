package com.example.cinema.service;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.AuditoriumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Auditorium findByNumber(int number) {
        return repository.findByNumber(number).orElseThrow(() -> new RequestException("Could not find auditorium with number: " + number));
    }

    public Auditorium addAuditorium(Auditorium auditorium) {
        if (repository.findByNumber(auditorium.getNumber()).isPresent()){
            throw new RequestException(String.format("Auditorium with number %s already exists", auditorium.getNumber()));
        }
        auditorium.setId(null);
        return repository.save(auditorium);
    }

    public void deleteAuditorium(Long id) {
        repository.deleteById(id);
    }
}
