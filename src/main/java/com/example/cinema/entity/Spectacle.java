package com.example.cinema.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Spectacle {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "spectacle")
    private List<Reservation> reservations;

    @ManyToOne
    private Movie movie;

    private LocalDateTime dateTime;
}
