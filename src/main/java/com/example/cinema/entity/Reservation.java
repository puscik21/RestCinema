package com.example.cinema.entity;

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
    private Spectacle spectacle;

    @ManyToOne
    private Seat seat;

    @ManyToOne
    private Spectator spectator;

    public Reservation(Spectacle spectacle, Seat seat, Spectator spectator) {
        this.spectacle = spectacle;
        this.seat = seat;
        this.spectator = spectator;
    }
}
