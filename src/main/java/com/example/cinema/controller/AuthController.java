package com.example.cinema.controller;

import com.example.cinema.dto.RegisterUserDTO;
import com.example.cinema.service.MappingService;
import com.example.cinema.service.SpectatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    // TODO: 6/27/2022 split spectator and user databases

    private final SpectatorService spectatorService;
    private final MappingService mappingService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody  @Valid RegisterUserDTO registerUserDTO) {
        spectatorService.save(mappingService.map(registerUserDTO));
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/register").toUriString());
        return ResponseEntity.created(uri).build();
    }
}
