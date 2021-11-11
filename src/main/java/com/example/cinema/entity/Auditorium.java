package com.example.cinema.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Auditorium {

    @Id
    private Long id;

    @OneToMany(mappedBy = "auditorium", orphanRemoval = true)
    private List<Seat> seats;

    private int number;

    public Auditorium(int number, int numberOfSeats) {
        this.number = number;
        seats = new ArrayList<>(numberOfSeats);
        for (int seatNumber = 1; seatNumber <= numberOfSeats; seatNumber++) {
            seats.add(new Seat(seatNumber));
        }
    }
}
