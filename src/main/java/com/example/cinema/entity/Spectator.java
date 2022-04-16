package com.example.cinema.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Spectator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "spectator")
    @JsonManagedReference(value = "spectator_reservations")
    private List<Reservation> reservations;

    @NotNull(message = "Spectator name cannot be null")
    private String name;

    @Column(unique = true)
    @NotNull(message = "Spectator email cannot be null")
    @Email(message = "Wrong email format")
    private String email;

    @NotNull(message = "Spectator phone number cannot be null")
    private String phoneNumber;

    public Spectator(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.reservations = new ArrayList<>();
    }
}
