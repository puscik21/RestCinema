package com.example.cinema.repository;

import com.example.cinema.entity.Spectator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpectatorRepository extends JpaRepository<Spectator, Long> {
}
