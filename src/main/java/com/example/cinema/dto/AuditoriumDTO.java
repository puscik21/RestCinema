package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuditoriumDTO {
    private Long id;
    private List<SeatDTO> seatDTOs;
    private List<SpectacleDTO> spectacleDTOs;
    private Integer number;
}
