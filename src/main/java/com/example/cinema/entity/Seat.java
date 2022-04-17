package com.example.cinema.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

    @ManyToOne(optional = false)
    private Auditorium auditorium;

    @OneToMany(mappedBy = "seat")
    private List<Reservation> reservations;

    @Min(value = 1, message = "Seat number must be greater then 0")
    @NotNull(message = "Seat number cannot be null")
    private int number;
    private boolean isReserved = false;

    public Seat(int number) {
        this.number = number;
        this.isReserved = false;
        this.reservations = new ArrayList<>();
    }
}
