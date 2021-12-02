package com.example.cinema.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // note: IDENTITY generation disables batch updates
    private Long id;

    @OneToMany(mappedBy = "auditorium", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "auditorium_seats")
    private List<Seat> seats;

    @OneToMany(mappedBy = "auditorium")
    @JsonManagedReference(value = "auditorium_spectacles")
    private List<Spectacle> spectacles;

    @Min(value = 1, message = "Auditorium number must be greater then 0")
    @NotNull(message = "Auditorium number cannot be null")
    private Integer number;

    public Auditorium(int number, int numberOfSeats) {
        this.number = number;
        seats = new ArrayList<>(numberOfSeats);
    }

    public Seat getSeat(int number) {
        return seats.get(number - 1);
    }
}
