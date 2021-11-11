package com.example.cinema.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Auditorium auditorium;

    @OneToMany(mappedBy = "seat")
    private List<Reservation> reservations;

    private int number;
    private boolean isReserved;

    public Seat(int number) {
        this.number = number;
        this.isReserved = false;
        this.reservations = new ArrayList<>();
    }
}
