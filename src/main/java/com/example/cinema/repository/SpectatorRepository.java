package com.example.cinema.repository;

import com.example.cinema.entity.Spectator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpectatorRepository extends JpaRepository<Spectator, Long> {
    @Query(value = "SELECT s from Spectator s where s.email = :email")
    Optional<Spectator> findByEmail(@Param("email") String email);
}
