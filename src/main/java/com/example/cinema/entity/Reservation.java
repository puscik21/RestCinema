package com.example.cinema.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Spectacle spectacle;

    @ManyToOne
    private Seat seat;

    @ManyToOne
    private Spectator spectator;
}
