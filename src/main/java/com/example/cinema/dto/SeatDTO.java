package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SeatDTO {
    private Long id;
    private Long auditoriumId;
    private List<ReservationDTO> reservationDTOs;

    @Min(value = 1, message = "Seat number must be greater then 0")
    @NotNull(message = "Seat number cannot be null")
    private int number;
    private boolean isReserved = false;
}
