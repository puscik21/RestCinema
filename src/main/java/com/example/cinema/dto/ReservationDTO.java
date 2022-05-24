package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
    private Long id;

    @NotNull(message = "Reservation seatId cannot be null")
    private Long seatId;

    @NotNull(message = "Reservation spectacleId cannot be null")
    private Long spectacleId;

    @NotNull(message = "Reservation spectatorId cannot be null")
    private Long spectatorId;
}
