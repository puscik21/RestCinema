package com.example.cinema.service;

import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Seat;
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
        checkIfDependenciesExist(reservation);
        Seat seat = seatService.findByIdOrThrow(reservation.getSeat().getId());
        checkIfSeatIsReserved(seat);
        seat.setReserved(true);
        reservation.setId(null);
        return repository.save(reservation);
    }

    private void checkIfDependenciesExist(Reservation reservation) {
        spectacleService.findByIdOrThrow(reservation.getSpectacle().getId());
        spectatorService.findByIdOrThrow(reservation.getSpectator().getId());
    }

    private void checkIfSeatIsReserved(Seat seat) {
        if (seat.isReserved()) {
            throw new RequestException(String.format("Seat with number %s is already reserved", seat.getNumber()));
        }
    }

    public void deleteReservation(Long id) {
        repository.deleteById(id);
    }
}
