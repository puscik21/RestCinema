package com.example.cinema.repository;

import com.example.cinema.entity.Spectacle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpectacleRepository extends JpaRepository<Spectacle, Long> {
}
