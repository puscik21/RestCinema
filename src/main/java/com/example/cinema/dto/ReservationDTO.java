package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
    private Long id;
    private Long spectacleId;
    private Long seatId;
    private Long spectatorId;
}
