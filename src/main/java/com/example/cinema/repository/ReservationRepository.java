package com.example.cinema.repository;

import com.example.cinema.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "SELECT r from Reservation r WHERE r.spectacle.id = :spectacleId AND r.seat.id = :seatId AND r.spectator.id = :spectatorId")
    Optional<Reservation> findByDependenciesIds(@Param("spectacleId") Long spectacleId, @Param("seatId") Long seatId, @Param("spectatorId") Long spectatorId);
}
