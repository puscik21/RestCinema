package com.example.cinema.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Spectacle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "spectacle")
    @JsonManagedReference(value = "spectacle_reservations")
    private List<Reservation> reservations;

    @ManyToOne
    @JsonBackReference(value = "movie_spectacles")
    private Movie movie;

    @ManyToOne
    @JsonBackReference(value = "auditorium_spectacles")
    private Auditorium auditorium;

    private LocalDateTime dateTime;

    public Spectacle(Movie movie, Auditorium auditorium, LocalDateTime dateTime) {
        this.movie = movie;
        this.auditorium = auditorium;
        this.dateTime = dateTime;
        this.reservations = new LinkedList<>();
    }
}
