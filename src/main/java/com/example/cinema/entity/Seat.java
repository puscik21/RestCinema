package com.example.cinema.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Seat {

    @Id
    @GeneratedValue
    private Long id;

    private int number;
    private boolean isTaken;

    // TODO: 23.10.2021 to be implemented
//    private Spectator spectator
}
