package com.example.cinema.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // note: IDENTITY generation disables batch updates
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference(value = "auditorium_seats")
    private Auditorium auditorium;

    @OneToMany(mappedBy = "seat")
    @JsonManagedReference(value = "seat_reservations")
    private List<Reservation> reservations;

    private int number;
    private boolean isReserved;

    public Seat(int number) {
        this.number = number;
        this.isReserved = false;
        this.reservations = new ArrayList<>();
    }
}
