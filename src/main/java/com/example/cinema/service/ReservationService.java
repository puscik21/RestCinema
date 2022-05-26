package com.example.cinema.service;

import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Seat;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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

    public Reservation save(Reservation reservation) {
        checkIfDependenciesExist(reservation);
        Seat seat = seatService.findByIdOrThrow(reservation.getSeat().getId());
        checkIfIsReserved(seat);
        seat.setReserved(true);
        reservation.setId(null);
        return repository.save(reservation);
    }

    private void checkIfDependenciesExist(Reservation reservation) {
        spectacleService.findByIdOrThrow(reservation.getSpectacle().getId());
        spectatorService.findByIdOrThrow(reservation.getSpectator().getId());
    }

    private void checkIfIsReserved(Seat seat) {
        if (seat.isReserved()) {
            throw new RequestException(String.format("Seat with number %s is already reserved", seat.getNumber()));
        }
    }

    public Map<String, String> deleteById(Long id) {
        Reservation reservation = findByIdOrThrow(id);
        log.info("Deleting reservation with id: {}", id);
        repository.delete(reservation);
        return Map.of("message", String.format("Reservation with id: %s has been removed", id));
    }
}
