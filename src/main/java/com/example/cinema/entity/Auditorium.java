package com.example.cinema.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // note: IDENTITY generation disables batch updates
    private Long id;

    @OneToMany(mappedBy = "auditorium", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Seat> seats;

    @OneToMany(mappedBy = "auditorium")
    private List<Spectacle> spectacles;

    @Column(unique = true, nullable = false)
    private Integer number;

    public Auditorium(int number, int numberOfSeats) {
        this.number = number;
        seats = new ArrayList<>(numberOfSeats);
    }
}
