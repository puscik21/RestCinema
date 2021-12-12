package com.example.cinema;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Movie;
import com.example.cinema.entity.Reservation;
import com.example.cinema.entity.Seat;
import com.example.cinema.entity.Spectacle;
import com.example.cinema.entity.Spectator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MockService {

    public Auditorium prepareAuditoriumWithSeat() {
        Auditorium auditorium = prepareAuditorium();
        Seat seat = prepareSeat();
        auditorium.setSeats(List.of(seat));
        return auditorium;
    }

    public Seat prepareSeat() {
        Seat seat = new Seat();
        seat.setAuditorium(prepareAuditorium());
        seat.setNumber(8);
        seat.setReserved(false);
        return seat;
    }

    public Auditorium prepareAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setNumber(8);
        return auditorium;
    }

    public Movie prepareMovie() {
        Movie movie = new Movie();
        movie.setName("movie");
        return movie;
    }

    public Spectator prepareSpectator() {
        Spectator spectator = new Spectator();
        spectator.setName("John");
        spectator.setEmail("Smith");
        spectator.setPhoneNumber("123456789");
        return spectator;
    }

    public Spectacle prepareSpectacle() {
        Spectacle spectacle = new Spectacle();
        spectacle.setDateTime(LocalDateTime.parse("2021-12-12T22:25:02"));
        return spectacle;
    }

    public Reservation prepareReservation() {
        Reservation reservation = new Reservation();
        reservation.setSeat(prepareSeat());
        return reservation;
    }
}
