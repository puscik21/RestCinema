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
public class AuditoriumDTO {
    private Long id;
    private List<SeatDTO> seatDTOs;
    private List<SpectacleDTO> spectacleDTOs;

    @Min(value = 1, message = "Auditorium number must be greater then 0")
    @NotNull(message = "Auditorium number cannot be null")
    private Integer number;
}
