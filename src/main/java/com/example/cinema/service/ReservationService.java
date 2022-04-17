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
        checkIfReservationAlreadyExists(reservation);
        Seat seat = seatService.findByIdOrThrow(reservation.getSeat().getId());
        checkIfSeatWithIdIsReserved(seat);
        seat.setReserved(true);
        reservation.setId(null);
        return repository.save(reservation);
    }

    private void checkIfDependenciesExist(Reservation reservation) {
        if (spectacleService.findById(reservation.getSpectacle().getId()).isEmpty()) {
            throw new RequestException(String.format("Could not find spectacle with id:  %s", reservation.getSpectacle().getId()));
        }
        if (seatService.findById(reservation.getSeat().getId()).isEmpty()) {
            throw new RequestException(String.format("Could not find seat with id:  %s", reservation.getSeat().getId()));
        }
        if (spectatorService.findById(reservation.getSpectator().getId()).isEmpty()) {
            throw new RequestException(String.format("Could not find spectator with id:  %s", reservation.getSpectator().getId()));
        }
    }

    private void checkIfReservationAlreadyExists(Reservation reservation) {
        if (repository.findByDependenciesIds(reservation.getSpectacle().getId(),
                        reservation.getSeat().getId(),
                        reservation.getSpectator().getId())
                .isPresent()) {
            throw new RequestException(String.format("Reservation already exists for email: %s",
                    spectatorService.findByIdOrThrow(reservation.getSpectator().getId()).getEmail()));
        }
    }

    private void checkIfSeatWithIdIsReserved(Seat seat) {
        if (seat.isReserved()) {
            throw new RequestException(String.format("Seat with number %s is already reserved", seat.getNumber()));
        }
    }

    public void deleteReservation(Long id) {
        repository.deleteById(id);
    }
}
