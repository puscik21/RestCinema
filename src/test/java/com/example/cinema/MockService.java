package com.example.cinema;

import com.example.cinema.entity.Auditorium;
import com.example.cinema.entity.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockService {

    public Seat prepareSeat() {
        Seat seat = new Seat();
        seat.setAuditorium(prepareAuditorium());
        seat.setNumber(8);
        seat.setReserved(false);
        return seat;
    }

    public Auditorium prepareAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setNumber(8);
        return auditorium;
    }
}
