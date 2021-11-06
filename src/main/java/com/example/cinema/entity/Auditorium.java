package com.example.cinema.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Auditorium {

    @Id
    private Long id;

    @OneToMany(mappedBy = "auditorium", orphanRemoval = true)
    private List<Seat> seats;

    private int number;
}
