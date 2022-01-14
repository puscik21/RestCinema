package com.example.cinema.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference(value = "spectacle_reservations")
    private Spectacle spectacle;

    @ManyToOne
    @JsonBackReference(value = "seat_reservations")
    private Seat seat;

    @ManyToOne
    @JsonBackReference(value = "spectator_reservations")
    private Spectator spectator;

    public Reservation(Spectacle spectacle, Seat seat, Spectator spectator) {
        this.spectacle = spectacle;
        this.seat = seat;
        this.spectator = spectator;
    }
}
