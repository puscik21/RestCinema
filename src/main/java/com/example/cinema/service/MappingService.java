package com.example.cinema.service;

import com.example.cinema.dto.*;
import com.example.cinema.entity.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MappingService {
    private final ModelMapper mapper;

    public AuditoriumDTO map(Auditorium auditorium) {
        AuditoriumDTO auditoriumDTO = mapper.map(auditorium, AuditoriumDTO.class);
        auditoriumDTO.setSeatDTOs(auditorium.getSeats().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        auditoriumDTO.setSpectacleDTOs(auditorium.getSpectacles().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return auditoriumDTO;
    }

    public MovieDTO map(Movie movie) {
        MovieDTO movieDTO = mapper.map(movie, MovieDTO.class);
        movieDTO.setSpectacleDTOs(movie.getSpectacles().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return movieDTO;
    }

    public ReservationDTO map(Reservation reservation) {
        return mapper.map(reservation, ReservationDTO.class);
    }

    public SeatDTO map(Seat seat) {
        SeatDTO seatDTO = mapper.map(seat, SeatDTO.class);
        seatDTO.setReservationDTOs(seat.getReservations().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return seatDTO;
    }

    public SpectacleDTO map(Spectacle spectacle) {
        SpectacleDTO spectacleDTO = mapper.map(spectacle, SpectacleDTO.class);
        spectacleDTO.setReservationDTOs(spectacle.getReservations().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return spectacleDTO;
    }

    public SpectatorDTO map(Spectator spectator) {
        SpectatorDTO spectatorDTO = mapper.map(spectator, SpectatorDTO.class);
        spectatorDTO.setReservationDTOs(spectator.getReservations().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return spectatorDTO;
    }

    public Auditorium map(AuditoriumDTO auditoriumDTO) {
        Auditorium auditorium = mapper.map(auditoriumDTO, Auditorium.class);
        auditorium.setSeats(auditoriumDTO.getSeatDTOs().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        auditorium.setSpectacles(auditoriumDTO.getSpectacleDTOs().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return auditorium;
    }

    public Movie map(MovieDTO movieDTO) {
        Movie movie = mapper.map(movieDTO, Movie.class);
        movie.setSpectacles(movieDTO.getSpectacleDTOs().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return movie;
    }

    public Reservation map(ReservationDTO reservationDTO) {
        return mapper.map(reservationDTO, Reservation.class);
    }

    public Seat map(SeatDTO seatDTO) {
        Seat seat = mapper.map(seatDTO, Seat.class);
        seat.setReservations(seatDTO.getReservationDTOs().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return seat;
    }

    public Spectacle map(SpectacleDTO spectacleDTO) {
        Spectacle spectacle = mapper.map(spectacleDTO, Spectacle.class);
        spectacle.setReservations(spectacleDTO.getReservationDTOs().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return spectacle;
    }

    public Spectator map(SpectatorDTO spectatorDTO) {
        Spectator spectator = mapper.map(spectatorDTO, Spectator.class);
        spectator.setReservations(spectatorDTO.getReservationDTOs().stream()
                .map(this::map)
                .collect(Collectors.toList()));
        return spectator;
    }
}
