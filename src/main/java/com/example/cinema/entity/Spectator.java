package com.example.cinema.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Spectator {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Seat> reservations;

    private String name;
    private String email;
    private int phoneNumber;
}
