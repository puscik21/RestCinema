package com.example.cinema.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RegisterUserDTO {
    @NotNull(message = "Spectator name cannot be null")
    private String name;

    @Email(message = "Wrong email format")
    @NotNull(message = "Spectator email cannot be null")
    private String email;

    @NotNull(message = "Spectator phone number cannot be null")
    private String phoneNumber;

    @NotNull(message = "Spectator password cannot be null")
    private String password;

    private String roles;
}
