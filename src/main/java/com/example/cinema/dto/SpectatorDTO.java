package com.example.cinema.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SpectatorDTO {
    private Long id;
    private List<ReservationDTO> reservationDTOs;

    @NotNull(message = "Spectator name cannot be null")
    private String name;

    @Email(message = "Wrong email format")
    @NotNull(message = "Spectator email cannot be null")
    private String email;

    @NotNull(message = "Spectator phone number cannot be null")
    private String phoneNumber;
}
