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

    private final Auditorium auditorium;
    private final Movie movie;
    private final Reservation reservation;
    private final Seat seat;
    private final Spectacle spectacle;
    private final Spectator spectator;

    public MockService() {
        auditorium = prepareAuditorium();
        movie = prepareMovie();
        reservation = prepareReservation();
        seat = prepareSeat();
        spectacle = prepareSpectacle();
        spectator = prepareSpectator();
        setAuditoriumDependencies();
        setMovieDependencies();
        setReservationDependencies();
        setSeatDependencies();
        setSpectacleDependencies();
        setSpectatorDependencies();
    }

    private Auditorium prepareAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setId(99L);
        auditorium.setNumber(8);
        return auditorium;
    }

    private Movie prepareMovie() {
        Movie movie = new Movie();
        movie.setId(99L);
        movie.setName("movie");
        return movie;
    }

    private Reservation prepareReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(99L);
        return reservation;
    }

    private Seat prepareSeat() {
        Seat seat = new Seat();
        seat.setId(99L);
        seat.setNumber(8);
        seat.setReserved(false);
        return seat;
    }

    private Spectacle prepareSpectacle() {
        Spectacle spectacle = new Spectacle();
        spectacle.setId(99L);
        spectacle.setDateTime(LocalDateTime.parse("2021-12-12T22:25:02"));
        return spectacle;
    }

    private Spectator prepareSpectator() {
        Spectator spectator = new Spectator();
        spectator.setId(99L);
        spectator.setName("John");
        spectator.setEmail("johnsmith@gmail.com");
        spectator.setPhoneNumber("123456789");
        return spectator;
    }

    public void setAuditoriumDependencies() {
        auditorium.setSpectacles(List.of(spectacle));
        auditorium.setSeats(List.of(seat));
    }

    public void setMovieDependencies() {
        movie.setSpectacles(List.of(spectacle));
    }

    public void setReservationDependencies() {
        reservation.setSeat(seat);
        reservation.setSpectacle(spectacle);
        reservation.setSpectator(spectator);
    }

    public void setSeatDependencies() {
        seat.setAuditorium(auditorium);
        seat.setReservations(List.of(reservation));
    }

    public void setSpectacleDependencies() {
        spectacle.setAuditorium(auditorium);
        spectacle.setMovie(movie);
        spectacle.setReservations(List.of(reservation));
    }

    public void setSpectatorDependencies() {
        spectator.setReservations(List.of(reservation));
    }

    public Auditorium getAuditorium() {
        return auditorium;
    }

    public Movie getMovie() {
        return movie;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Seat getSeat() {
        return seat;
    }

    public Spectacle getSpectacle() {
        return spectacle;
    }

    public Spectator getSpectator() {
        return spectator;
    }

    public List<Auditorium> prepareAuditoriumsList() {
        return List.of(prepareAuditorium(), prepareAuditorium());
    }

    public List<Movie> prepareMoviesList() {
        return List.of(prepareMovie(), prepareMovie());
    }

    public List<Reservation> prepareReservationsList() {
        return List.of(prepareReservation(), prepareReservation());
    }

    public List<Seat> prepareSeatsList() {
        return List.of(prepareSeat(), prepareSeat());
    }

    public List<Spectacle> prepareSpectaclesList() {
        return List.of(prepareSpectacle(), prepareSpectacle());
    }

    public List<Spectator> prepareSpectatorsList() {
        return List.of(prepareSpectator(), prepareSpectator());
    }
}
