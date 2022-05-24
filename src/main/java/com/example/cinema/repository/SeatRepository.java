package com.example.cinema.repository;

import com.example.cinema.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query(value = "SELECT s from Seat s WHERE s.number = :number")
    Optional<Seat> findByNumber(@Param("number") int anyInt);

    // TODO: 06.12.2021 try to find by number and auditorium
}
