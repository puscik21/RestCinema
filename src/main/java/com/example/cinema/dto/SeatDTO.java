package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SeatDTO {
    private Long id;
    private Long auditoriumId;
    private List<ReservationDTO> reservationDTOs;
    private int number;
    private boolean isReserved = false;
}
