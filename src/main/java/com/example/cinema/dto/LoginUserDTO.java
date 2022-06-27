package com.example.cinema.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginUserDTO {

    @Email(message = "Wrong email format")
    @NotNull(message = "Spectator email cannot be null")
    private String email;

    @NotNull(message = "Spectator password cannot be null")
    private String password;
}
