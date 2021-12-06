package com.example.cinema.service;

import com.example.cinema.entity.Reservation;
import com.example.cinema.exception.RequestException;
import com.example.cinema.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;
    private final SeatService seatService;

    public ReservationService(ReservationRepository repository, SeatService seatService) {
        this.repository = repository;
        this.seatService = seatService;
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public Reservation findById(Long id) throws RequestException {
        return repository.findById(id).orElseThrow(() -> new RequestException("Could not find reservation with id: " + id));
    }

    public Reservation addReservation(Reservation reservation) {
        return repository.save(reservation);
    }

    public void deleteReservation(Long id) {
        repository.deleteById(id);
    }
}
