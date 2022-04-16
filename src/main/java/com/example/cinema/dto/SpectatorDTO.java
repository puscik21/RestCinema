package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpectatorDTO {
    private Long id;
    private List<ReservationDTO> reservationDTOs;
    private String name;
    private String email;
    private String phoneNumber;
}
