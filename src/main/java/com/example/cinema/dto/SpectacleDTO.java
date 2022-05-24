package com.example.cinema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpectacleDTO {
    private Long id;
    private List<ReservationDTO> reservationDTOs;

    @NotNull(message = "Spectacle auditoriumId cannot be null")
    private Long auditoriumId;

    @NotNull(message = "Spectacle movieId cannot be null")
    private Long movieId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Spectacle dateTime cannot be null")
    private LocalDateTime dateTime;
}
