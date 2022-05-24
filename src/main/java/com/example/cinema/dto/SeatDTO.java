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
public class SeatDTO { // TODO: 5/22/2022 dto interface
    private Long id;

    @NotNull(message = "Seat auditoriumId cannot be null")
    private Long auditoriumId;
    private List<ReservationDTO> reservationDTOs;

    @Min(value = 1, message = "Seat number must be greater then 0")
    @NotNull(message = "Seat number cannot be null")
    private Integer number;

    @NotNull(message = "Seat isReserved flag cannot be null")
    private Boolean isReserved = false;
}
