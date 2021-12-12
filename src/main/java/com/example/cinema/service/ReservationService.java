package com.example.cinema.service;

import com.example.cinema.entity.Reservation;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;
    private final SpectacleService spectacleService;
    private final SpectatorService spectatorService;
    private final SeatService seatService;

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public Reservation findByIdOrThrow(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find reservation with id: " + id));
    }

    public Optional<Reservation> findById(Long id) {
        return repository.findById(id);
    }

    public Reservation addReservation(Reservation reservation) {
        return repository.save(reservation);
    }

    public void deleteReservation(Long id) {
        repository.deleteById(id);
    }
}
