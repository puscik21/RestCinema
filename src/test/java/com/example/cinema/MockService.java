package com.example.cinema;

import com.example.cinema.entity.Seat;
import org.springframework.stereotype.Service;

@Service
public class MockService {

    public Seat prepareSeat() {
        Seat seat = new Seat();
        seat.setNumber(8);
        seat.setReserved(false);
        return seat;
    }
}
