package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private List<SpectacleDTO> spectacleDTOs;
    private String name;
}
