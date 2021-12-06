package com.example.cinema.repository;

import com.example.cinema.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {

    @Query(value = "SELECT a FROM Auditorium a WHERE a.number = :number")
    Optional<Auditorium> findByNumber(@Param("number") Integer number);
}
