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

    // TODO: 4/16/2022 try to make more DRY code (use interface i.e.)
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
}
