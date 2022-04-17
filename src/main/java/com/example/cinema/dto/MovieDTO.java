package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private List<SpectacleDTO> spectacleDTOs;

    @NotNull(message = "Movie name cannot be null")
    private String name;
}
