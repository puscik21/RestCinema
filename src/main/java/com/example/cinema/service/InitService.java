package com.example.cinema.service;

import com.example.cinema.entity.Spectator;
import com.example.cinema.repository.SpectatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitService implements CommandLineRunner {
    private final SpectatorRepository spectatorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Spectator spectator = new Spectator();
        spectator.setName("admin");
        spectator.setEmail("admin@mail.com");
        spectator.setPhoneNumber("123456789");
        spectator.setPassword(passwordEncoder.encode("admin"));
        spectator.setRoles("ADMIN");
        spectatorRepository.save(spectator);
    }
}
